package com.mindera.rocketscience.company.data.remote.spacex.source

import com.mindera.rocketscience.company.data.remote.spacex.model.SpacexCompany
import retrofit2.http.GET

interface SpacexCompanyRestAPI {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): SpacexCompany
}