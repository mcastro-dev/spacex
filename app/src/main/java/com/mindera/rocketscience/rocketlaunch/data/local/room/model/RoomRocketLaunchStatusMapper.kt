package com.mindera.rocketscience.rocketlaunch.data.local.room.model

import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

object RoomRocketLaunchStatusMapper {
    fun toDomain(status: String) : RocketLaunch.Status {
        return RocketLaunch.Status.valueOf(status)
    }

    fun fromDomain(status: RocketLaunch.Status) : String {
        return status.name
    }
}