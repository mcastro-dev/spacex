package com.mindera.rocketscience.rocketlaunch.data

import com.mindera.rocketscience.rocketlaunch.data.error.GetFilterFailed
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.times

class RepositoryGetFilterTest : RocketLaunchRepositoryBaseTest() {

    @Test
    fun getFilter_success() = runBlocking {
        given(localFilterDataSource.getFilter())
            .willReturn(filter)

        val whenResult = repository.getFilter()

        then(localFilterDataSource).should(times(1)).getFilter()
        assert(whenResult.isSuccess)
        assertEquals(filter, whenResult.getOrNull())
    }

    @Test
    fun getFilter_failure() = runBlocking {
        val error = RuntimeException("Error saving")
        given(localFilterDataSource.getFilter())
            .willThrow(error)

        assertThrows(GetFilterFailed::class.java) {
            runBlocking {
                repository.getFilter()
            }
        }

        then(localFilterDataSource).should(times(1)).getFilter()
        Unit
    }

}