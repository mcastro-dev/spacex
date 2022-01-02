package com.mindera.rocketscience.home.itself.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindera.rocketscience.common.data.service.INetworkConnectivityService
import com.mindera.rocketscience.common.di.SingletonDIModule
import com.mindera.rocketscience.common.domain.error.NoInternetAccess
import com.mindera.rocketscience.common.domain.error.ServerDown
import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.presentation.viewmodel.BaseViewModel
import com.mindera.rocketscience.common.presentation.viewmodel.ShowErrorCommand
import com.mindera.rocketscience.common.presentation.viewmodel.UIEvent
import com.mindera.rocketscience.company.domain.usecase.IGetCompanyInfo
import com.mindera.rocketscience.home.company.model.UICompany
import com.mindera.rocketscience.home.itself.domain.usecase.IRefreshHome
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.usecase.GetRocketLaunches
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IGetRocketLaunches
import com.mindera.rocketscience.home.rocketlaunch.itself.model.UIRocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.event.FiltersChanged
import com.mindera.rocketscience.rocketlaunch.domain.event.IRocketLaunchEventsPublisher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @SingletonDIModule.DispatchersIO
    private val dispatcher: CoroutineDispatcher,
    private val domainEventsPublisher: IRocketLaunchEventsPublisher,
    private val networkConnectivityService: INetworkConnectivityService,
    private val getCompanyInfo: IGetCompanyInfo,
    private val getRocketLaunches: IGetRocketLaunches,
    private val refreshHome: IRefreshHome
) : BaseViewModel<HomeState>() {

    private val compositeDisposable = CompositeDisposable()
    private var _currentPage = Page.first()
    private var _reachedLastPage = false

    val currentPage: Page get() = _currentPage
    val pageSize: Int get() = _currentPage.expectedItemsPerPage
    val isLoadingMoreItems: Boolean get() = _state.value?.isLoadingRocketLaunches ?: false
    val reachedLastPage: Boolean get() = _reachedLastPage

    init {
        _state.value = HomeState()
        observeDomainEvents()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    override fun onEvent(event: UIEvent) {
        viewModelScope.launch(dispatcher) {
            when(event) {
                is GetInitialDataEvent -> executeGetInitialDataEvent()
                is GetNextRocketLaunchesEvent -> executeGetNextRocketLaunchesEvent()
                is RefreshEvent -> executeRefreshEvent()
            }
        }
    }

    private fun observeDomainEvents() {
        domainEventsPublisher.events()
            .filter { it is FiltersChanged }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModelScope.launch(dispatcher) {
                    executeRefreshEvent()
                }
            }.addTo(compositeDisposable)
    }

    private suspend fun executeGetInitialDataEvent() {
        getRocketLaunches(forPage = _currentPage)
        getCompanyInfo()
    }

    private suspend fun executeGetNextRocketLaunchesEvent() {
        onIsLoadingRocketLaunches {
            return
        }

        _currentPage = _currentPage.next()

        getRocketLaunches(forPage = _currentPage)
    }

    private suspend fun executeRefreshEvent() {
        onIsRefreshing {
            return
        }

        setState(_state.value!!.copy(
            isRefreshing = true,
            isLoadingRocketLaunches = true,
            isLoadingCompanyInfo = true
        ))
        _currentPage = Page.first()
        _reachedLastPage = false

        val result = refreshHome(Unit)

        // Failure
        if (result.isFailure) {
            handleError(result.exceptionOrNull()!!)
            setState(_state.value!!.copy(
                isRefreshing = false,
                isLoadingRocketLaunches = false,
                isLoadingCompanyInfo = false,
                hasStaleData = _state.value!!.rocketLaunches.isNotEmpty()
            ))
            return
        }

        // Success
        result.getOrNull()!!.run {
            val uiCompany = value.company?.let { UICompany.fromDomain(it) }
            val uiLaunches = value.rocketLaunches.map { UIRocketLaunch.fromDomain(it) }

            setState(_state.value!!.copy(
                isRefreshing = false,
                isLoadingRocketLaunches = false,
                isLoadingCompanyInfo = false,
                company = uiCompany,
                rocketLaunches = uiLaunches,
                hasStaleData = state == Data.State.STALE
            ))
        }
    }

    private suspend fun getCompanyInfo() {
        onIsLoadingCompanyInfo {
            return
        }

        setState(_state.value!!.copy(isLoadingCompanyInfo = true))

        val result = getCompanyInfo(Unit)
        if (result.isFailure) {
            handleError(result.exceptionOrNull()!!)
            setState(_state.value!!.copy(
                isLoadingCompanyInfo = false
            ))
            return
        }

        val companyInfo = result.getOrNull()!!.value
        val uiCompany = UICompany.fromDomain(companyInfo)
        setState(_state.value!!.copy(
            isLoadingCompanyInfo = false,
            company = uiCompany
        ))
    }

    private fun handleError(error: Throwable) {
        // matheus: I think implementing this parsing as an http interceptor would be a better approach
        //  for when we happen to have more view models
        // matheus: TODO: implement as http interceptor if there's enough time
        val parsedError = if (error is UnreachableServer) {
            val hasInternet = networkConnectivityService.hasNetworkConnectivity()
            if (hasInternet) ServerDown() else NoInternetAccess()
        } else {
            error
        }
        emitCommand(ShowErrorCommand(parsedError))
    }

    private suspend fun getRocketLaunches(forPage: Page) {
        onIsLoadingRocketLaunches {
            return
        }

        setState(_state.value!!.copy(isLoadingRocketLaunches = true))

        val result = getRocketLaunches(GetRocketLaunches.Params(forPage))

        if (result.isFailure) {
            handleError(result.exceptionOrNull()!!)
            setState(_state.value!!.copy(
                isLoadingRocketLaunches = false,
                hasStaleData = _state.value!!.rocketLaunches.isNotEmpty()
            ))
            return
        }

        val data = result.getOrNull()!!
        val rocketLaunches = data.value

        if (rocketLaunches.isNullOrEmpty()) {
            _reachedLastPage = true
        }

        val newUiLaunches = rocketLaunches.map { UIRocketLaunch.fromDomain(it) }
        val oldUiLaunches = _state.value!!.rocketLaunches
        val joinedUiLaunches = oldUiLaunches.plus(newUiLaunches)

        setState(_state.value!!.copy(
            isLoadingRocketLaunches = false,
            rocketLaunches = joinedUiLaunches,
            hasStaleData = data.state == Data.State.STALE
        ))
    }

    private inline fun onIsRefreshing(block: () -> Unit) {
        if (_state.value!!.isRefreshing) {
            block.invoke()
        }
    }

    private inline fun onIsLoadingCompanyInfo(block: () -> Unit) {
        if (_state.value!!.isLoadingCompanyInfo) {
            block.invoke()
        }
    }

    private inline fun onIsLoadingRocketLaunches(block: () -> Unit) {
        if (_state.value!!.isLoadingRocketLaunches) {
            block.invoke()
        }
    }
}