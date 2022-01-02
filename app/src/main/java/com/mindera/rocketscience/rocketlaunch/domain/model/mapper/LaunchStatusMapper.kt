package com.mindera.rocketscience.rocketlaunch.domain.model.mapper

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

object LaunchStatusMapper {
     fun fromFilterMissionStatus(filterMissionStatus: Filter.MissionStatus) : RocketLaunch.Status {
         return when(filterMissionStatus) {
             Filter.MissionStatus.SUCCESS -> RocketLaunch.Status.SUCCESS
             Filter.MissionStatus.FAILURE -> RocketLaunch.Status.FAILURE
             else -> throw RuntimeException("Unexpected mission status")
         }
     }
}