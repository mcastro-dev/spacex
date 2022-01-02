package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.rocketlaunch.domain.error.UnableToGetFilters
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*

class GetRocketLaunchesTest : LaunchesUseCaseBaseTest() {

    private lateinit var getRocketLaunches: IGetRocketLaunches

    override fun setUp() {
        super.setUp()
        getRocketLaunches = GetRocketLaunches(repository)
    }

    @Test
    fun getLaunches_success() = runBlocking {
        given(repository.getFilter())
            .willReturn(Result.success(filter))
        given(repository.getRocketLaunches(forPage = page, withFilter = filter))
            .willReturn(Result.success(Data(Data.State.FRESH, launches)))

        val whenResult = getRocketLaunches(GetRocketLaunches.Params(page))

        then(repository).should(times(1))
            .getFilter()
        then(repository).should(times(1))
            .getRocketLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isSuccess)
        assertEquals(launches, whenResult.getOrNull()?.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()?.state)
    }

    @Test
    fun getLaunches_getFilterSuccess_getLaunchesFailure() = runBlocking {
        val error = RuntimeException()
        given(repository.getFilter())
            .willReturn(Result.success(filter))
        given(repository.getRocketLaunches(forPage = page, withFilter = filter))
            .willReturn(Result.failure(error))

        val whenResult = getRocketLaunches(GetRocketLaunches.Params(page))

        then(repository).should(times(1))
            .getFilter()
        then(repository).should(times(1))
            .getRocketLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull())
    }

    @Test
    fun getLaunches_getFilterFailure() = runBlocking {
        val error = RuntimeException()
        given(repository.getFilter()).willReturn(Result.failure(error))

        val whenResult = getRocketLaunches(GetRocketLaunches.Params(page))

        then(repository).should(times(1)).getFilter()
        then(repository).should(never())
            .getRocketLaunches(forPage = any(), withFilter = any())
        assert(whenResult.isFailure)
        assertEquals(
            UnableToGetFilters::class.java,
            whenResult.exceptionOrNull()!!::class.java
        )
    }

}