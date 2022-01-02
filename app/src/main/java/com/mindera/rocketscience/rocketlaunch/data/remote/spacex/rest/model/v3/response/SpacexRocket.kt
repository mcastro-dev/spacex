package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v3.response

import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket

data class SpacexRocket(
    @SerializedName("rocket_id")
    val id: String,
    @SerializedName("rocket_name")
    val name: String,
    @SerializedName("rocket_type")
    val type: String,
) {
    fun toDomain() : Rocket {
        return Rocket(id = Id(id), name = name, type = type)
    }
}