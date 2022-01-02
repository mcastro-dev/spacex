package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response

import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.common.data.adapter.DateTimeDataMapper
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper.RocketLaunchStatusMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

data class SpacexRocketLaunch(
    @SerializedName("id")
    val id: String,
    @SerializedName("flight_number")
    val flightNumber: Int,
    @SerializedName("name")
    val missionName: String,
    @SerializedName("date_utc")
    val launchDateUTC: String,
    @SerializedName("success")
    val launchSuccess: Boolean?,
    @SerializedName("rocket")
    val rocket: SpacexRocket,
    @SerializedName("links")
    val links: SpacexLinks,
) {
    fun toDomain(): RocketLaunch {
        return RocketLaunch(
            id = Id(id),
            missionName = missionName,
            launchDateTime = DateTimeDataMapper.fromString(launchDateUTC),
            rocket = rocket.toDomain(),
            status = RocketLaunchStatusMapper.toDomain(launchSuccess),
            patchImageLink = links.patchImage?.smallImageLink?.let { Link(it) },
            articleLink = links.articleLink?.let { Link(it) },
            wikipediaLink = links.wikipediaLink?.let { Link(it) },
            videoLink = links.videoLink?.let { Link(it) }
        )
    }
}