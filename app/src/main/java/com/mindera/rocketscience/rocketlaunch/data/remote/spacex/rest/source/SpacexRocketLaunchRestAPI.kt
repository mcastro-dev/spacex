package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.source

import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v3.response.SpacexRocketLaunch
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response.SpacexRocketLaunchesResponse
import retrofit2.http.*

interface SpacexRocketLaunchRestAPI {

    @GET("/v3/launches")
    suspend fun getRocketLaunches(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("order") order: String,
    ): List<SpacexRocketLaunch>

    @Headers("Content-Type: application/json")
    @POST("/v4/launches/query")
    suspend fun getRocketLaunches(@Body body: String): SpacexRocketLaunchesResponse
}