package com.mindera.rocketscience.company.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import javax.inject.Inject

interface IGetCompanyInfo : ICoroutineUseCase<Unit, Result<Data<Company>>>

class GetCompanyInfo @Inject constructor(
    private val repository: ICompanyRepository
) : IGetCompanyInfo {

    override suspend fun invoke(params: Unit): Result<Data<Company>> {
        return repository.getInfo()
    }
}