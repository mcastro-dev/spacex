package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.model

import com.mindera.rocketscience.GetRocketLaunchesQuery
import com.mindera.rocketscience.common.data.adapter.DateTimeDataMapper
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper.RocketLaunchStatusMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

fun GetRocketLaunchesQuery.Launch.toDomain() : RocketLaunch {
    return RocketLaunch(
        id = Id(id!!),
        missionName = mission_name!!,
        launchDateTime = DateTimeDataMapper.fromString(launch_date_utc as String),
        rocket = rocket!!.toDomain(),
        status = RocketLaunchStatusMapper.toDomain(launch_success),
        patchImageLink = links?.mission_patch_small?.let { Link(it) },
        articleLink = links?.article_link?.let { Link(it) },
        wikipediaLink = links?.wikipedia?.let { Link(it) },
        videoLink = links?.video_link?.let { Link(it) }
    )
}