package com.mindera.rocketscience.rocketlaunch.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.company.data.error.LocalSaveCompanyInfoFailed
import com.mindera.rocketscience.company.data.error.RemoteAndLocalGetCompanyInfoFailed
import com.mindera.rocketscience.rocketlaunch.data.error.LocalGetRocketLaunchesFailed
import com.mindera.rocketscience.rocketlaunch.data.error.LocalSaveRocketLaunchesFailed
import com.mindera.rocketscience.rocketlaunch.data.error.RemoteAndLocalGetRocketLaunchesFailed
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepositoryGetLaunchesTest : RocketLaunchRepositoryBaseTest() {

    @Test
    fun getLaunches_success() = runBlocking {
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.saveLaunches(launches))
            .willReturn(Unit)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)

        val whenResult = repository.getRocketLaunches(
            forPage = page,
            withFilter = filter
        )

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1))
            .saveLaunches(launches)
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isSuccess)
        assertEquals(launches, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()!!.state)
    }

    @Test
    fun getLaunches_remoteFailure_localSuccess() = runBlocking {
        val remoteError = RuntimeException("Remote error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(remoteError)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)

        val whenResult = repository.getRocketLaunches(forPage = page, withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never())
            .saveLaunches(any())
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isSuccess)
        assertEquals(launches, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.STALE, whenResult.getOrNull()!!.state)
    }

    @Test
    fun getLaunches_remoteFailure_localFailure() = runBlocking {
        val remoteError = RuntimeException("Remote error")
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(remoteError)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(localError)

        Assert.assertThrows(RemoteAndLocalGetRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.getRocketLaunches(forPage = page, withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never())
            .saveLaunches(any())
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        Unit
    }

    @Test
    fun getLaunches_remoteSocketTimeOutException_localFailure() = runBlocking {
        val remoteError = SocketTimeoutException()
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willAnswer { throw remoteError }
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(localError)

        val whenResult = repository.getRocketLaunches(forPage = page, withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never())
            .saveLaunches(any())
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun getLaunches_remoteUnknownHostException_localFailure() = runBlocking {
        val remoteError = UnknownHostException()
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willAnswer { throw remoteError }
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(localError)

        val whenResult = repository.getRocketLaunches(forPage = page, withFilter = filter)

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(never())
            .saveLaunches(any())
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun getLaunches_remoteSuccess_localSaveFailure() = runBlocking {
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.saveLaunches(launches))
            .willThrow(localError)

        Assert.assertThrows(LocalSaveRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.getRocketLaunches(forPage = page, withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1))
            .saveLaunches(launches)
        then(localDataSource).should(never())
            .getLaunches(forPage = page, withFilter = filter)
        Unit
    }

    @Test
    fun getLaunches_remoteSuccess_localSaveSuccess_localGetFailure() = runBlocking {
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getLaunches(forPage = page, withFilter = filter))
            .willReturn(launches)
        given(localDataSource.saveLaunches(launches))
            .willReturn(Unit)
        given(localDataSource.getLaunches(forPage = page, withFilter = filter))
            .willThrow(localError)

        Assert.assertThrows(LocalGetRocketLaunchesFailed::class.java) {
            runBlocking {
                repository.getRocketLaunches(forPage = page, withFilter = filter)
            }
        }

        then(remoteDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        then(localDataSource).should(times(1))
            .saveLaunches(launches)
        then(localDataSource).should(times(1))
            .getLaunches(forPage = page, withFilter = filter)
        Unit
    }
}