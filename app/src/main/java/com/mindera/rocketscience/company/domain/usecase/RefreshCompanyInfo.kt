package com.mindera.rocketscience.company.domain.usecase

import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import javax.inject.Inject

interface IRefreshCompanyInfo : ICoroutineUseCase<Unit, Result<Company>>

class RefreshCompanyInfo @Inject constructor(
    private val companyRepository: ICompanyRepository
) : IRefreshCompanyInfo {

    override suspend fun invoke(params: Unit): Result<Company> {
        return companyRepository.forceRefresh()
    }
}