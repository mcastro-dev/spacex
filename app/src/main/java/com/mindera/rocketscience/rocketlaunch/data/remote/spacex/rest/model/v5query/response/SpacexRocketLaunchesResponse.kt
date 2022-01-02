package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response

import com.google.gson.annotations.SerializedName

data class SpacexRocketLaunchesResponse(
    @SerializedName(value = "docs")
    val documents: List<SpacexRocketLaunch>
)