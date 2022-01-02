package com.mindera.rocketscience.home.itself.viewmodel

import com.mindera.rocketscience.common.data.service.INetworkConnectivityService
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.common.presentation.viewmodel.ShowErrorCommand
import com.mindera.rocketscience.common.presentation.viewmodel.ViewModelBaseTest
import com.mindera.rocketscience.common.utils.UnitCallback
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year
import com.mindera.rocketscience.company.domain.usecase.IGetCompanyInfo
import com.mindera.rocketscience.home.company.model.UICompany
import com.mindera.rocketscience.home.itself.domain.model.Home
import com.mindera.rocketscience.home.itself.domain.usecase.IRefreshHome
import com.mindera.rocketscience.home.rocketlaunch.itself.model.UIRocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.event.IRocketLaunchEventsPublisher
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.usecase.GetRocketLaunches
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IGetRocketLaunches
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.then
import org.mockito.kotlin.times
import java.time.LocalDateTime
import java.time.Month

@ExperimentalCoroutinesApi
class HomeViewModelTest : ViewModelBaseTest() {

    private lateinit var networkConnectivityService: INetworkConnectivityService
    private lateinit var domainEventsPublisher: IRocketLaunchEventsPublisher
    private lateinit var getCompanyInfo: IGetCompanyInfo
    private lateinit var getRocketLaunches: IGetRocketLaunches
    private lateinit var refreshHome: IRefreshHome
    private lateinit var viewModel: HomeViewModel

    private val company = Company(
        "Mindera",
        "Paul Evans",
        Year(2014),
        500,
        8,
        Money(100000000)
    )
    private val launches = listOf(
        RocketLaunch(
            id = Id("1"),
            missionName = "FalconSat",
            launchDateTime = LocalDateTime.of(2016, Month.MARCH, 24, 22, 30),
            rocket = Rocket(id = Id("falcon1"), name = "Falcon 1", type = "Merlin A"),
            status = RocketLaunch.Status.FAILURE,
            patchImageLink = Link("https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"),
            articleLink = Link("https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html"),
            wikipediaLink = Link("https://en.wikipedia.org/wiki/DemoSat"),
            videoLink = Link("https://www.youtube.com/watch?v=0a_00nJ_Y88")
        ),
        RocketLaunch(
            id = Id("2"),
            missionName = "Future",
            launchDateTime = LocalDateTime.of(3000, Month.JANUARY, 1, 12, 0),
            rocket = Rocket(id = Id("future0"), name = "Future Zero", type = "Future Zero A"),
            status = RocketLaunch.Status.TO_BE_DONE
        )
    )
    private val home = Home(
        company = company,
        rocketLaunches = launches
    )

    override fun setUp() {
        super.setUp()

        networkConnectivityService = mock()
        domainEventsPublisher = mock()
        getCompanyInfo = mock()
        getRocketLaunches = mock()
        refreshHome = mock()
    }

    @Test
    fun initialState() = runTest {
        val initialState = HomeState(
            isRefreshing = false,
            isLoadingCompanyInfo = false,
            isLoadingRocketLaunches = false,
            hasStaleData = false,
            company = null,
            rocketLaunches = emptyList()
        )
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        assertEquals(initialState, viewModel.state.value)
    }

    @Test
    fun getInitialDataEvent_success() = runTest {
        val page = Page.first()
        val uiLaunches = launches.map { UIRocketLaunch.fromDomain(it) }
        val uiCompany = UICompany.fromDomain(company)
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(getRocketLaunches(GetRocketLaunches.Params(page))).willAnswer {
            Result.success(Data(Data.State.FRESH, launches))
        }
        given(getCompanyInfo(Unit)).willAnswer {
            Result.success(Data(Data.State.FRESH, company))
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(GetInitialDataEvent())
        scheduler.advanceUntilIdle()

        then(getRocketLaunches).should(times(1))
            .invoke(GetRocketLaunches.Params(page))
        then(getCompanyInfo).should(times(1)).invoke(Unit)
        assertEquals(5, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(HomeState(isLoadingRocketLaunches = true), states[1])
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                rocketLaunches = uiLaunches,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                rocketLaunches = uiLaunches,
                hasStaleData = false,
                isLoadingCompanyInfo = true
            ),
            states[3]
        )
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                rocketLaunches = uiLaunches,
                hasStaleData = false,
                isLoadingCompanyInfo = false,
                company = uiCompany
            ),
            states[4]
        )
        assertEquals(0, commands.size)
    }

    @Test
    fun getInitialDataEvent_getLaunchesFailure() = runTest {
        val error = RuntimeException("Error GET launches")
        val page = Page.first()
        val params = GetRocketLaunches.Params(page)
        val uiCompany = UICompany.fromDomain(company)
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(getRocketLaunches(params)).willAnswer {
            Result.failure<Any>(error)
        }
        given(getCompanyInfo(Unit)).willAnswer {
            Result.success(Data(Data.State.FRESH, company))
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(GetInitialDataEvent())
        scheduler.advanceUntilIdle()

        then(getRocketLaunches).should(times(1)).invoke(params)
        then(getCompanyInfo).should(times(1)).invoke(Unit)
        assertEquals(5, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(HomeState(isLoadingRocketLaunches = true), states[1])
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                hasStaleData = false,
                isLoadingCompanyInfo = true
            ),
            states[3]
        )
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                hasStaleData = false,
                isLoadingCompanyInfo = false,
                company = uiCompany
            ),
            states[4]
        )
        assertEquals(1, commands.size)
        assertEquals(ShowErrorCommand::class, commands[0]::class)
        assertEquals(error, (commands[0] as ShowErrorCommand).error)
    }

    @Test
    fun getInitialDataEvent_getCompanyFailure() = runTest {
        val error = RuntimeException("Error GET company")
        val page = Page.first()
        val params = GetRocketLaunches.Params(page)
        val uiLaunches = launches.map { UIRocketLaunch.fromDomain(it) }
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(getRocketLaunches(params)).willAnswer {
            Result.success(Data(Data.State.FRESH, launches))
        }
        given(getCompanyInfo(Unit)).willAnswer {
            Result.failure<Any>(error)
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(GetInitialDataEvent())
        scheduler.advanceUntilIdle()

        then(getRocketLaunches).should(times(1)).invoke(params)
        then(getCompanyInfo).should(times(1)).invoke(Unit)
        assertEquals(5, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(
            states[0].copy(
                isLoadingRocketLaunches = true
            ),
            states[1]
        )
        assertEquals(
            states[1].copy(
                isLoadingRocketLaunches = false,
                rocketLaunches = uiLaunches,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(
            states[2].copy(
                isLoadingCompanyInfo = true
            ),
            states[3]
        )
        assertEquals(
            states[3].copy(
                isLoadingCompanyInfo = false
            ),
            states[4]
        )
        assertEquals(1, commands.size)
        assertEquals(ShowErrorCommand::class, commands[0]::class)
        assertEquals(error, (commands[0] as ShowErrorCommand).error)
    }

    @Test
    fun getNextRocketLaunchesEvent_success() = runTest {
        val nextPage = Page.first().next()
        val params = GetRocketLaunches.Params(nextPage)
        val uiLaunches = launches.map { UIRocketLaunch.fromDomain(it) }
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(getRocketLaunches(params)).willAnswer {
            Result.success(Data(Data.State.FRESH, launches))
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(GetNextRocketLaunchesEvent())
        scheduler.advanceUntilIdle()

        then(getRocketLaunches).should(times(1)).invoke(params)
        assertEquals(3, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(HomeState(isLoadingRocketLaunches = true), states[1])
        assertEquals(
            HomeState(
                isLoadingRocketLaunches = false,
                rocketLaunches = uiLaunches,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(0, commands.size)
        assertEquals(nextPage, viewModel.currentPage)
    }

    @Test
    fun getNextRocketLaunchesEvent_failure() = runTest {
        val error = RuntimeException("Error GET launches")
        val nextPage = Page.first().next()
        val params = GetRocketLaunches.Params(nextPage)
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(getRocketLaunches(params)).willAnswer {
            Result.failure<Any>(error)
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(GetNextRocketLaunchesEvent())
        scheduler.advanceUntilIdle()

        then(getRocketLaunches).should(times(1)).invoke(params)
        assertEquals(3, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(
            states[0].copy(
                isLoadingRocketLaunches = true
            ),
            states[1]
        )
        assertEquals(
            states[1].copy(
                isLoadingRocketLaunches = false,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(1, commands.size)
        assertEquals(ShowErrorCommand::class, commands[0]::class)
        assertEquals(error, (commands[0] as ShowErrorCommand).error)
        assertEquals(nextPage, viewModel.currentPage)
    }

    @Test
    fun refreshEvent_success() = runTest {
        val page = Page.first()
        val uiLaunches = launches.map { UIRocketLaunch.fromDomain(it) }
        val uiCompany = UICompany.fromDomain(company)
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(refreshHome(Unit)).willAnswer {
            Result.success(Data(Data.State.FRESH, home))
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(RefreshEvent())
        scheduler.advanceUntilIdle()

        then(refreshHome).should(times(1)).invoke(Unit)
        assertEquals(3, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(
            HomeState(
                isRefreshing = true,
                isLoadingRocketLaunches = true,
                isLoadingCompanyInfo = true
            ),
            states[1]
        )
        assertEquals(
            HomeState(
                isRefreshing = false,
                isLoadingRocketLaunches = false,
                isLoadingCompanyInfo = false,
                company = uiCompany,
                rocketLaunches = uiLaunches,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(0, commands.size)
        assertEquals(page, viewModel.currentPage)
    }

    @Test
    fun refreshEvent_failure() = runTest {
        val error = RuntimeException("Error REFRESH data")
        initViewModel {
            given(domainEventsPublisher.events()).willReturn(Flowable.empty())
        }
        given(refreshHome(Unit)).willAnswer {
            Result.failure<Any>(error)
        }

        observeStates()
        observeCommands()
        viewModel.onEvent(RefreshEvent())
        scheduler.advanceUntilIdle()

        then(refreshHome).should(times(1)).invoke(Unit)
        assertEquals(3, states.size)
        assertEquals(HomeState(), states[0])
        assertEquals(
            states[0].copy(
                isRefreshing = true,
                isLoadingRocketLaunches = true,
                isLoadingCompanyInfo = true
            ),
            states[1]
        )
        assertEquals(
            states[0].copy(
                isRefreshing = false,
                isLoadingRocketLaunches = false,
                isLoadingCompanyInfo = false,
                hasStaleData = false
            ),
            states[2]
        )
        assertEquals(1, commands.size)
        assertEquals(ShowErrorCommand::class, commands[0]::class)
        assertEquals(error, (commands[0] as ShowErrorCommand).error)
        assertEquals(Page.first(), viewModel.currentPage)
    }

    override fun observeStates() {
        viewModel.state.observeForever { state ->
            states.add(state)
        }
    }

    override fun observeCommands() {
        viewModel.command.subscribe { command ->
            commands.add(command)
        }
    }

    private inline fun initViewModel(before: UnitCallback) {
        before.invoke()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(
            dispatcher = dispatcher,
            domainEventsPublisher = domainEventsPublisher,
            networkConnectivityService = networkConnectivityService,
            getCompanyInfo = getCompanyInfo,
            getRocketLaunches = getRocketLaunches,
            refreshHome = refreshHome
        )
    }
}