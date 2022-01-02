package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response

import com.google.gson.annotations.SerializedName

data class SpacexEngine(
    @SerializedName(value = "type")
    val type: String
)