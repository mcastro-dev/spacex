package com.mindera.rocketscience.company.data.remote.spacex.source

import com.mindera.rocketscience.company.data.remote.IRemoteCompanyDataSource
import com.mindera.rocketscience.company.domain.model.Company
import javax.inject.Inject

class SpacexCompanyRestDataSource @Inject constructor(
    private val restAPI: SpacexCompanyRestAPI
) : IRemoteCompanyDataSource {

    override suspend fun getInfo(): Company {
        val spacexCompany = restAPI.getCompanyInfo()
        return spacexCompany.toDomain()
    }
}