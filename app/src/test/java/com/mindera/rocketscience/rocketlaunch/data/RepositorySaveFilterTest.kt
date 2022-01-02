package com.mindera.rocketscience.rocketlaunch.data

import com.mindera.rocketscience.rocketlaunch.data.error.SaveFilterFailed
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.times

class RepositorySaveFilterTest : RocketLaunchRepositoryBaseTest() {

    @Test
    fun saveFilter_success() = runBlocking {
        given(localFilterDataSource.saveFilter(filter))
            .willReturn(Unit)

        val whenResult = repository.saveFilter(filter)

        then(localFilterDataSource).should(times(1)).saveFilter(filter)
        assert(whenResult.isSuccess)
        assertEquals(filter, whenResult.getOrNull())
    }

    @Test
    fun saveFilter_failure() = runBlocking {
        val error = RuntimeException("Error saving")
        given(localFilterDataSource.saveFilter(filter))
            .willThrow(error)

        assertThrows(SaveFilterFailed::class.java) {
            runBlocking {
                repository.saveFilter(filter)
            }
        }

        then(localFilterDataSource).should(times(1)).saveFilter(filter)
    }
}