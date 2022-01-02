package com.mindera.rocketscience.company.data.local

import com.mindera.rocketscience.company.domain.model.Company

interface ILocalCompanyDataSource {
    suspend fun saveInfo(company: Company)
    suspend fun getInfo(): Company
    suspend fun deleteInfo()
}