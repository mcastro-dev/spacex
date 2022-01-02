package com.mindera.rocketscience.company.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.company.data.error.LocalSaveCompanyInfoFailed
import com.mindera.rocketscience.company.data.error.RemoteAndLocalGetCompanyInfoFailed
import com.mindera.rocketscience.company.data.local.ILocalCompanyDataSource
import com.mindera.rocketscience.company.data.remote.IRemoteCompanyDataSource
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepositoryGetInfoTest : CompanyRepositoryBaseTest() {

    @Test
    fun getInfo_success() = runBlocking {
        given(remoteDataSource.getInfo()).willReturn(company)
        given(localDataSource.saveInfo(company)).willReturn(Unit)

        val whenResult = repository.getInfo()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(times(1)).saveInfo(company)
        assert(whenResult.isSuccess)
        assertEquals(company, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()!!.state)
    }

    @Test
    fun getInfo_remoteFailure_localSuccess() = runBlocking {
        val error = RuntimeException("Remote error")
        given(remoteDataSource.getInfo()).willThrow(error)
        given(localDataSource.getInfo()).willReturn(company)

        val whenResult = repository.getInfo()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).saveInfo(any())
        then(localDataSource).should(times(1)).getInfo()
        assert(whenResult.isSuccess)
        assertEquals(company, whenResult.getOrNull()!!.value)
        assertEquals(Data.State.STALE, whenResult.getOrNull()!!.state)
    }

    @Test
    fun getInfo_remoteFailure_localFailure() = runBlocking {
        val remoteError = RuntimeException("Remote error")
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getInfo()).willThrow(remoteError)
        given(localDataSource.getInfo()).willThrow(localError)

        assertThrows(RemoteAndLocalGetCompanyInfoFailed::class.java) {
            runBlocking {
                repository.getInfo()
            }
        }

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).saveInfo(any())
        then(localDataSource).should(times(1)).getInfo()
        Unit
    }

    @Test
    fun getInfo_remoteSocketTimeOutException_localFailure() = runBlocking {
        val remoteError = SocketTimeoutException()
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getInfo()).willAnswer { throw remoteError }
        given(localDataSource.getInfo()).willThrow(localError)

        val whenResult = repository.getInfo()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).saveInfo(any())
        then(localDataSource).should(times(1)).getInfo()
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun getInfo_remoteUnknownHostException_localFailure() = runBlocking {
        val remoteError = UnknownHostException()
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getInfo()).willAnswer { throw remoteError }
        given(localDataSource.getInfo()).willThrow(localError)

        val whenResult = repository.getInfo()

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(never()).saveInfo(any())
        then(localDataSource).should(times(1)).getInfo()
        assert(whenResult.isFailure)
        assertEquals(
            UnreachableServer::class,
            whenResult.exceptionOrNull()!!::class
        )
    }

    @Test
    fun getInfo_remoteSuccess_localFailure() = runBlocking {
        val localError = RuntimeException("Local error")
        given(remoteDataSource.getInfo()).willReturn(company)
        given(localDataSource.saveInfo(company)).willThrow(localError)

        assertThrows(LocalSaveCompanyInfoFailed::class.java) {
            runBlocking {
                repository.getInfo()
            }
        }

        then(remoteDataSource).should(times(1)).getInfo()
        then(localDataSource).should(times(1)).saveInfo(company)
        then(localDataSource).should(never()).getInfo()
        Unit
    }
}