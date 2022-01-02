package com.mindera.rocketscience.home.itself.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year
import com.mindera.rocketscience.company.domain.usecase.IRefreshCompanyInfo
import com.mindera.rocketscience.home.itself.domain.model.Home
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IRefreshRocketLaunches
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.time.Month

class RefreshHomeTest {

    private lateinit var refreshHome: IRefreshHome
    private lateinit var refreshRocketLaunches: IRefreshRocketLaunches
    private lateinit var refreshCompanyInfo: IRefreshCompanyInfo

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

    @Before
    fun setUp() {
        refreshRocketLaunches = mock()
        refreshCompanyInfo = mock()
        refreshHome = RefreshHome(refreshRocketLaunches, refreshCompanyInfo)
    }

    @Test
    fun refreshHome_success() = runBlocking {
        given(refreshCompanyInfo.invoke(Unit)).willAnswer { Result.success(company) }
        given(refreshRocketLaunches.invoke(Unit))
            .willAnswer { Result.success(Data(Data.State.FRESH, launches)) }

        val whenResult = refreshHome(Unit)

        then(refreshCompanyInfo).should(times(1)).invoke(Unit)
        then(refreshRocketLaunches).should(times(1)).invoke(Unit)
        assertTrue(whenResult.isSuccess)
        assertEquals(home, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()!!.state)
    }

    @Test
    fun refreshHome_companyFailure() = runBlocking {
        val error = RuntimeException("Error refreshing company")
        given(refreshCompanyInfo.invoke(Unit)).willAnswer {
            Result.failure<Company>(error)
        }

        val whenResult = refreshHome(Unit)

        then(refreshCompanyInfo).should(times(1)).invoke(Unit)
        then(refreshRocketLaunches).should(never()).invoke(Unit)
        assertTrue(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull()!!)
    }

    @Test
    fun refreshHome_rocketLaunchesFailure() = runBlocking {
        val error = RuntimeException("Error refreshing rocket launches")
        given(refreshCompanyInfo.invoke(Unit)).willAnswer { Result.success(company) }
        given(refreshRocketLaunches.invoke(Unit)).willAnswer {
            Result.failure<Data<List<RocketLaunch>>>(error)
        }

        val whenResult = refreshHome(Unit)

        then(refreshCompanyInfo).should(times(1)).invoke(Unit)
        then(refreshRocketLaunches).should(times(1)).invoke(Unit)
        assertTrue(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull()!!)
    }
}