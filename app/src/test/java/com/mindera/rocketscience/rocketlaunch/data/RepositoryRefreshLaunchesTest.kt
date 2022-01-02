package com.mindera.rocketscience.rocketlaunch.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.rocketlaunch.data.error.*
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepositoryRefreshLaunchesTest : RocketLaunchRepositoryBaseTest() {

    @Test
    fun refreshLaunches_success() = runBlocking {
        val localLaunches = launches.toList()
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.deleteAllRocketLaunches()).willReturn(Unit)
        given(localDataSource.deleteAllRockets()).willReturn(Unit)
        given(localDataSource.saveLaunches(launches)).willReturn(Unit)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(localLaunches)

        val whenResult = repository.refreshRocketLaunches(
            withFilter = filter
        )

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(times(1)).deleteAllRockets()
        then(localDataSource).should(times(1)).saveLaunches(launches)
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isSuccess)
        assertEquals(localLaunches, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()!!.state)
    }

    @Test
    fun refreshLaunches_remoteFailure() = runBlocking {
        val remoteError = RuntimeException("Remote GET error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(remoteError)

        assertThrows(RemoteGetRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.refreshRocketLaunches(withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never()).deleteAllRocketLaunches()
        then(localDataSource).should(never()).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(any())
        then(localDataSource).should(never()).getLaunches(forPage = page, withFilter = filter)
        Unit
    }

    @Test
    fun refreshLaunches_remoteSocketTimeoutException() = runBlocking {
        val remoteError = SocketTimeoutException()
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willAnswer { throw remoteError }

        val whenResult = repository.refreshRocketLaunches(withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never()).deleteAllRocketLaunches()
        then(localDataSource).should(never()).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(any())
        then(localDataSource).should(never()).getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun refreshLaunches_remoteUnknownHostException() = runBlocking {
        val remoteError = UnknownHostException()
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willAnswer { throw remoteError }

        val whenResult = repository.refreshRocketLaunches(withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never()).deleteAllRocketLaunches()
        then(localDataSource).should(never()).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(any())
        then(localDataSource).should(never()).getLaunches(forPage = any(), withFilter = any())
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun refreshLaunches_remoteSuccess_localDeleteLaunchesFailure() = runBlocking {
        val localError = RuntimeException("Local DELETE launches error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.deleteAllRocketLaunches()).willThrow(localError)

        val whenResult = repository.refreshRocketLaunches(
            withFilter = filter
        )

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(never()).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(launches)
        then(localDataSource).should(never()).getLaunches(forPage = any(), withFilter = any())
        assert(whenResult.isFailure)
        assertEquals(
            LocalDeleteAllRocketLaunchesFailed::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun refreshLaunches_remoteSuccess_localDeleteRocketsFailure() = runBlocking {
        val localError = RuntimeException("Local DELETE rockets error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.deleteAllRocketLaunches()).willReturn(Unit)
        given(localDataSource.deleteAllRockets()).willThrow(localError)

        assertThrows(LocalDeleteAllRocketsFailed::class.java) {
            runBlocking {
                repository.refreshRocketLaunches(withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(times(1)).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(launches)
        then(localDataSource).should(never()).getLaunches(forPage = any(), withFilter = any())
        Unit
    }

    @Test
    fun refreshLaunches_remoteSuccess_localSaveLaunchesFailure() = runBlocking {
        val localError = RuntimeException("Local SAVE launches error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.deleteAllRocketLaunches()).willReturn(Unit)
        given(localDataSource.deleteAllRockets()).willReturn(Unit)
        given(localDataSource.saveLaunches(launches)).willThrow(localError)

        assertThrows(LocalSaveRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.refreshRocketLaunches(withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(times(1)).deleteAllRockets()
        then(localDataSource).should(times(1)).saveLaunches(launches)
        then(localDataSource).should(never()).getLaunches(forPage = any(), withFilter = any())
        Unit
    }

    @Test
    fun refreshLaunches_remoteSuccess_localGetLaunchesFailure() = runBlocking {
        val localError = RuntimeException("Local GET launches error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.deleteAllRocketLaunches()).willReturn(Unit)
        given(localDataSource.deleteAllRockets()).willReturn(Unit)
        given(localDataSource.saveLaunches(launches)).willReturn(Unit)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(localError)

        assertThrows(LocalGetRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.refreshRocketLaunches(withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(times(1)).deleteAllRockets()
        then(localDataSource).should(times(1)).saveLaunches(launches)
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        Unit
    }

    @Test
    fun refreshLaunches_remoteSuccess_isEmpty() = runBlocking {
        val emptyLaunchesList = emptyList<RocketLaunch>()
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(emptyLaunchesList)
        given(localDataSource.deleteAllRocketLaunches()).willReturn(Unit)
        given(localDataSource.deleteAllRockets()).willReturn(Unit)

        val whenResult = repository.refreshRocketLaunches(withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1)).deleteAllRocketLaunches()
        then(localDataSource).should(times(1)).deleteAllRockets()
        then(localDataSource).should(never()).saveLaunches(any())
        then(localDataSource).should(never())
            .getLaunches(forPage = any(), withFilter = any())
        assertTrue(whenResult.isSuccess)
        assertTrue(whenResult.getOrNull()!!.value.isEmpty())
        assertEquals(Data.State.FRESH, whenResult.getOrNull()!!.state)
    }
}