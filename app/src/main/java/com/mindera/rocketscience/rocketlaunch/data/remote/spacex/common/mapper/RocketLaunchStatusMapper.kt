package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper

import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

object RocketLaunchStatusMapper {
    fun toDomain(launchSuccess: Boolean?) : RocketLaunch.Status {
        return if (launchSuccess == null) {
            RocketLaunch.Status.TO_BE_DONE
        } else if (launchSuccess) {
            RocketLaunch.Status.SUCCESS
        } else {
            RocketLaunch.Status.FAILURE
        }
    }
}