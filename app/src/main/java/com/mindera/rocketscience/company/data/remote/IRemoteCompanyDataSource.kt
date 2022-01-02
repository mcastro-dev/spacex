package com.mindera.rocketscience.company.data.remote

import com.mindera.rocketscience.company.domain.model.Company

interface IRemoteCompanyDataSource {
    suspend fun getInfo(): Company
}