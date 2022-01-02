package com.mindera.rocketscience.company.data

import com.mindera.rocketscience.company.data.local.ILocalCompanyDataSource
import com.mindera.rocketscience.company.data.remote.IRemoteCompanyDataSource
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import org.junit.Before
import org.mockito.kotlin.mock

abstract class CompanyRepositoryBaseTest {
    protected lateinit var remoteDataSource: IRemoteCompanyDataSource
    protected lateinit var localDataSource: ILocalCompanyDataSource
    protected lateinit var repository: ICompanyRepository

    protected val company = Company(
        "Mindera",
        "Paul Evans",
        Year(2014),
        500,
        8,
        Money(100000000)
    )

    @Before
    fun setUp() {
        remoteDataSource = mock()
        localDataSource = mock()
        repository = CompanyRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }
}