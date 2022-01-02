package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.mapper

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter

object LaunchSuccessMapper {
    fun fromDomain(missionStatus: Filter.MissionStatus) : Boolean? {
        return when(missionStatus) {
            Filter.MissionStatus.ANY -> null
            Filter.MissionStatus.SUCCESS -> true
            Filter.MissionStatus.FAILURE -> false
        }
    }
}