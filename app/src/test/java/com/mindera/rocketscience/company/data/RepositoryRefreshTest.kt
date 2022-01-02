package com.mindera.rocketscience.company.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.company.data.error.LocalDeleteCompanyInfoFailed
import com.mindera.rocketscience.company.data.error.LocalSaveCompanyInfoFailed
import com.mindera.rocketscience.company.data.error.RemoteGetCompanyInfoFailed
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.kotlin.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepositoryRefreshTest : CompanyRepositoryBaseTest() {

    @Test
    fun refresh_success() = runBlocking {
        given(remoteDataSource.getInfo()).willReturn(company)
        given(localDataSource.deleteInfo()).willReturn(Unit)
        given(localDataSource.saveInfo(company)).willReturn(Unit)

        val whenResult = repository.forceRefresh()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(times(1)).deleteInfo()
        then(localDataSource).should(times(1)).saveInfo(company)
        assert(whenResult.isSuccess)
        assertEquals(company, whenResult.getOrNull())
    }

    @Test
    fun refresh_remoteFailure() = runBlocking {
        val remoteError = RuntimeException("Remote error")
        given(remoteDataSource.getInfo()).willThrow(remoteError)

        assertThrows(RemoteGetCompanyInfoFailed::class.java) {
            runBlocking {
                repository.forceRefresh()
            }
        }

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).deleteInfo()
        then(localDataSource).should(never()).saveInfo(company)
    }

    @Test
    fun refresh_remoteSocketTimeOutException() = runBlocking {
        val remoteError = SocketTimeoutException()
        given(remoteDataSource.getInfo()).willAnswer { throw remoteError }

        val whenResult = repository.forceRefresh()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).deleteInfo()
        then(localDataSource).should(never()).saveInfo(company)
        assert(whenResult.isFailure)
        assertEquals(UnreachableServer::class, whenResult.exceptionOrNull()!!::class)
    }

    @Test
    fun refresh_remoteUnknownHostException() = runBlocking {
        val remoteError = UnknownHostException()
        given(remoteDataSource.getInfo()).willAnswer { throw remoteError }

        val whenResult = repository.forceRefresh()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).deleteInfo()
        then(localDataSource).should(never()).saveInfo(company)
        assert(whenResult.isFailure)
        assertEquals(UnreachableServer::class, whenResult.exceptionOrNull()!!::class)
    }

    @Test
    fun refresh_remoteSuccess_localDeleteFailure() = runBlocking {
        val localError = RuntimeException("Local DELETE error")
        given(remoteDataSource.getInfo()).willReturn(company)
        given(localDataSource.deleteInfo()).willThrow(localError)

        assertThrows(LocalDeleteCompanyInfoFailed::class.java) {
            runBlocking {
                repository.forceRefresh()
            }
        }

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(times(1)).deleteInfo()
        then(localDataSource).should(never()).saveInfo(any())
    }

    @Test
    fun refresh_remoteSuccess_localSaveFailure() = runBlocking {
        val localError = RuntimeException("Local SAVE error")
        given(remoteDataSource.getInfo()).willReturn(company)
        given(localDataSource.deleteInfo()).willReturn(Unit)
        given(localDataSource.saveInfo(company)).willThrow(localError)

        assertThrows(LocalSaveCompanyInfoFailed::class.java) {
            runBlocking {
                repository.forceRefresh()
            }
        }

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(times(1)).deleteInfo()
        then(localDataSource).should(times(1)).saveInfo(any())
    }
}