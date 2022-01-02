package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v3.response

import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.common.data.adapter.DateTimeDataMapper
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper.RocketLaunchStatusMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

data class SpacexRocketLaunch(
    @SerializedName("flight_number")
    val flightNumber: Int,
    @SerializedName("mission_name")
    val missionName: String,
    @SerializedName("launch_year")
    val launchYear: String,
    @SerializedName("launch_date_unix")
    val launchDateUnix: Long,
    @SerializedName("launch_date_utc")
    val launchDateUTC: String,
    @SerializedName("launch_date_local")
    val launchDateLocal: String,
    @SerializedName("rocket")
    val rocket: SpacexRocket,
    @SerializedName("launch_success")
    val launchSuccess: Boolean?,
    @SerializedName("links")
    val links: SpacexLinks,
) {
    fun toDomain(): RocketLaunch {
        return RocketLaunch(
            // matheus: Docs point out to a "flight_id" param, but there's no such thing coming from the API.
            //  Therefore, had to concatenate the "flight_number" and "mission_name" in order to have a unique identifier.
            //  "flight_number" is not unique because there may be more rockets being launched per flight (as far as I can understand from the responses).
            id = Id("${flightNumber}-${missionName}"),
            missionName = missionName,
            launchDateTime = DateTimeDataMapper.fromString(launchDateUTC),
            rocket = rocket.toDomain(),
            status = RocketLaunchStatusMapper.toDomain(launchSuccess),
            patchImageLink = links.patchImageLink?.let { Link(it) },
            articleLink = links.articleLink?.let { Link(it) },
            wikipediaLink = links.wikipediaLink?.let { Link(it) },
            videoLink = links.videoLink?.let { Link(it) }
        )
    }
}