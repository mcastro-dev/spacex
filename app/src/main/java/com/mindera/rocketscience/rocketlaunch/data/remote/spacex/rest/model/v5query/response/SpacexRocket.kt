package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response

import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket

data class SpacexRocket(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("engines")
    val engine: SpacexEngine,
) {
    fun toDomain() : Rocket {
        return Rocket(id = Id(id), name = name, type = engine.type)
    }
}
