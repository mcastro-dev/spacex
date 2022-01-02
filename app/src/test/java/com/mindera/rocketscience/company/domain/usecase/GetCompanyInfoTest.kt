package com.mindera.rocketscience.company.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.times

class GetCompanyInfoTest : CompanyUseCaseBaseTest() {

    private lateinit var getCompanyInfo: IGetCompanyInfo

    override fun setUp() {
        super.setUp()
        getCompanyInfo = GetCompanyInfo(repository)
    }

    @Test
    fun getInfo_success() = runBlocking {
        given(repository.getInfo())
            .willReturn(Result.success(Data(Data.State.FRESH, company)))

        val whenResult = getCompanyInfo(Unit)

        then(repository).should(times(1)).getInfo()
        assert(whenResult.isSuccess)
        assertEquals(company, whenResult.getOrNull()?.value)
        assertEquals(Data.State.FRESH, whenResult.getOrNull()?.state)
    }

    @Test
    fun getInfo_failure() = runBlocking {
        val error = RuntimeException()
        given(repository.getInfo()).willReturn(Result.failure(error))

        val whenResult = getCompanyInfo(Unit)

        then(repository).should(times(1)).getInfo()
        assert(whenResult.isFailure)
        assertEquals(error, whenResult.exceptionOrNull())
    }
}