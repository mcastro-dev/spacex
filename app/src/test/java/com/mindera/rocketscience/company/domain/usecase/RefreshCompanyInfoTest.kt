package com.mindera.rocketscience.company.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.times

class RefreshCompanyInfoTest : CompanyUseCaseBaseTest() {

    private lateinit var refreshCompanyInfo: IRefreshCompanyInfo

    override fun setUp() {
        super.setUp()
        refreshCompanyInfo = RefreshCompanyInfo(repository)
    }

    @Test
    fun getInfo_success() = runBlocking {
        given(repository.forceRefresh())
            .willReturn(Result.success(company))

        val whenResult = refreshCompanyInfo(Unit)

        then(repository).should(times(1)).forceRefresh()
        assert(whenResult.isSuccess)
        assertEquals(company, whenResult.getOrNull())
    }

    @Test
    fun getInfo_failure() = runBlocking {
        val error = RuntimeException()
        given(repository.forceRefresh()).willReturn(Result.failure(error))

        val whenResult = refreshCompanyInfo(Unit)

        then(repository).should(times(1)).forceRefresh()
        assert(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull())
    }
}