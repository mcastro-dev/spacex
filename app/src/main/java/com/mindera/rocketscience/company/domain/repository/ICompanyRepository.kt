package com.mindera.rocketscience.company.domain.repository

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.company.domain.model.Company

interface ICompanyRepository {
    suspend fun getInfo(): Result<Data<Company>>
    suspend fun forceRefresh(): Result<Company>
}