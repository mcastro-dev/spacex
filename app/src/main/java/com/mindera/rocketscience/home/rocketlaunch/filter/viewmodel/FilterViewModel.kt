package com.mindera.rocketscience.home.rocketlaunch.filter.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindera.rocketscience.common.di.SingletonDIModule
import com.mindera.rocketscience.common.presentation.viewmodel.BaseViewModel
import com.mindera.rocketscience.common.presentation.viewmodel.ShowErrorCommand
import com.mindera.rocketscience.common.presentation.viewmodel.UIEvent
import com.mindera.rocketscience.common.presentation.viewmodel.UIState
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IGetRocketLaunchesFilter
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IApplyRocketLaunchesFilter
import com.mindera.rocketscience.rocketlaunch.domain.usecase.ApplyRocketLaunchesFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    @SingletonDIModule.DispatchersIO
    private val dispatcher: CoroutineDispatcher,
    private val getFilter: IGetRocketLaunchesFilter,
    private val applyFilter: IApplyRocketLaunchesFilter
) : BaseViewModel<FilterViewModel.State>() {

    override fun onEvent(event: UIEvent) {
        viewModelScope.launch(dispatcher) {
            when(event) {
                is GetFiltersEvent -> handleGetFilterEvent()
                is ApplyFiltersEvent -> handleApplyFiltersEvent()
                is NextYearEvent -> handleNextYearEvent()
                is PreviousYearEvent -> handlePreviousYearEvent()
                is ChangeMissionStatusEvent -> handleChangeMissionStatusEvent(event)
                is ChangeSortOrderEvent -> handleChangeSortOrderEvent(event)
            }
        }
    }

    private suspend fun handleChangeSortOrderEvent(event: ChangeSortOrderEvent) {
        val updatedFilter = _state.value!!.filter.copy(sortOrder = event.sortOrder)
        setState(
            _state.value!!.copy(filter = updatedFilter)
        )
    }

    private suspend fun handleChangeMissionStatusEvent(event: ChangeMissionStatusEvent) {
        val updatedFilter = _state.value!!.filter.copy(missionStatus = event.missionStatus)
        setState(
            _state.value!!.copy(filter = updatedFilter)
        )
    }

    private suspend fun handlePreviousYearEvent() {
        val updatedFilter = _state.value!!.filter.previousYear()
        setState(
            _state.value!!.copy(filter = updatedFilter)
        )
    }

    private suspend fun handleNextYearEvent() {
        val updatedFilter = _state.value!!.filter.nextYear()
        setState(
            _state.value!!.copy(filter = updatedFilter)
        )
    }

    private suspend fun handleGetFilterEvent() {
        val result = getFilter(Unit)
        if (result.isFailure) {
            // matheus: seems ok to just swallow the error here,
            //  because there's a default filter in the view model state anyway.
            return
        }

        val filter = result.getOrNull()!!
        setState(State(filter = filter))
    }

    private suspend fun handleApplyFiltersEvent() {
        setState(_state.value!!.copy(savingStatus = State.SavingStatus.SAVING))

        val filter = _state.value!!.filter
        val result = applyFilter(ApplyRocketLaunchesFilter.Params(filter))

        if (result.isFailure) {
            emitCommand(ShowErrorCommand(result.exceptionOrNull()!!))
            setState(_state.value!!.copy(savingStatus = State.SavingStatus.IDLE))
            return
        }

        setState(_state.value!!.copy(savingStatus = State.SavingStatus.SAVED))
    }

    data class State(
        val filter: Filter = Filter(),
        val savingStatus: SavingStatus = SavingStatus.IDLE
    ) : UIState {

        val isApplyEnabled get() = savingStatus == SavingStatus.IDLE

        enum class SavingStatus {
            IDLE,
            SAVING,
            SAVED
        }
    }
}