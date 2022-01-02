package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.rocketlaunch.domain.error.UnableToGetFilters
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*

class RefreshRocketLaunchesTest : LaunchesUseCaseBaseTest() {

    private lateinit var refreshRocketLaunches: IRefreshRocketLaunches

    override fun setUp() {
        super.setUp()
        refreshRocketLaunches = RefreshRocketLaunches(repository)
    }

    @Test
    fun refreshLaunches_success() = runBlocking {
        given(repository.getFilter()).willReturn(Result.success(filter))
        given(repository.refreshRocketLaunches(withFilter = filter))
            .willReturn(Result.success(Data(Data.State.FRESH, launches)))

        val whenResult = refreshRocketLaunches(Unit)

        then(repository).should(times(1)).getFilter()
        then(repository).should(times(1)).refreshRocketLaunches(withFilter = filter)
        assert(whenResult.isSuccess)
        assertEquals(launches, whenResult.getOrNull()?.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()?.state)
    }

    @Test
    fun getLaunches_getFilterSuccess_getLaunchesFailure() = runBlocking {
        val error = RuntimeException("Error REFRESH launches")
        given(repository.getFilter()).willReturn(Result.success(filter))
        given(repository.refreshRocketLaunches(withFilter = filter))
            .willReturn(Result.failure(error))

        val whenResult = refreshRocketLaunches(Unit)

        then(repository).should(times(1)).getFilter()
        then(repository).should(times(1)).refreshRocketLaunches(withFilter = filter)
        assert(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull())
    }

    @Test
    fun getLaunches_getFilterFailure() = runBlocking {
        val error = RuntimeException("Error GET filter")
        given(repository.getFilter()).willReturn(Result.failure(error))

        val whenResult = refreshRocketLaunches(Unit)

        then(repository).should(times(1)).getFilter()
        then(repository).should(never()).refreshRocketLaunches(withFilter = any())
        assert(whenResult.isFailure)
        assertEquals(
            UnableToGetFilters::class.java,
            whenResult.exceptionOrNull()!!::class.java
        )
    }
}