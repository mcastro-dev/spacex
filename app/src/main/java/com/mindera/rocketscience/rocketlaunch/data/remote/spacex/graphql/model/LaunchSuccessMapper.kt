package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.model

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter

object LaunchSuccessMapper {
    fun fromDomain(missionStatus: Filter.MissionStatus) : String? {
        return when(missionStatus) {
            Filter.MissionStatus.ANY -> null
            Filter.MissionStatus.SUCCESS -> "true"
            Filter.MissionStatus.FAILURE -> "false"
        }
    }
}