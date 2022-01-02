package com.mindera.rocketscience.home.rocketlaunch.itself.model

import com.mindera.rocketscience.common.presentation.adapter.DateTimeUIMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import kotlin.math.abs

data class UIRocketLaunch(
    val id: String,
    val missionName: String,
    val launchDate: String,
    val absoluteDaysDiffFromToday: Long,
    val didAlreadyLaunch: Boolean,
    // matheus: decomposed the rocket here as I don't see much benefit in having separate classes for the UI in this case
    val rocketName: String,
    val rocketType: String,
    // matheus: I think there's no point in creating a "UIStatus" that would basically have to be identical to the domain VO we already have
    val status: RocketLaunch.Status,
    val patchImageLink: String? = null,
    val articleLink: String? = null,
    val wikipediaLink: String? = null,
    val videoLink: String? = null
) {
    companion object {

        fun fromDomain(rocketLaunch: RocketLaunch) : UIRocketLaunch {
            val daysDiffFromLaunchToToday = rocketLaunch.daysDiffFromLaunchToToday

            return UIRocketLaunch(
                id = rocketLaunch.id.value,
                missionName = rocketLaunch.missionName,
                launchDate = DateTimeUIMapper.toString(rocketLaunch.launchDateTime),
                absoluteDaysDiffFromToday = abs(daysDiffFromLaunchToToday),
                didAlreadyLaunch = daysDiffFromLaunchToToday < 0,
                rocketName = rocketLaunch.rocket.name,
                rocketType = rocketLaunch.rocket.type,
                status = rocketLaunch.status,
                patchImageLink = rocketLaunch.patchImageLink?.value,
                articleLink = rocketLaunch.articleLink?.value,
                wikipediaLink = rocketLaunch.wikipediaLink?.value,
                videoLink = rocketLaunch.videoLink?.value
            )
        }
    }
}