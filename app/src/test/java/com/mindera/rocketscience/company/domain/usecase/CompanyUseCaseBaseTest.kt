package com.mindera.rocketscience.company.domain.usecase

import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import org.junit.Before
import org.mockito.kotlin.mock

abstract class CompanyUseCaseBaseTest {

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
    open fun setUp() {
        repository = mock()
    }
}