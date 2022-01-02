package com.mindera.rocketscience.rocketlaunch.data.local.room.model

import androidx.room.Embedded
import androidx.room.Relation
import com.mindera.rocketscience.common.data.adapter.DateTimeDataMapper
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.domain.model.Link
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

data class RoomRocketLaunchAggregate(
    @Embedded
    val rocketLaunch: RoomRocketLaunch,
    @Relation(
        parentColumn = "rocketId",
        entityColumn = "id"
    )
    val rocket: RoomRocket
) {
    companion object {
        fun fromDomain(rocketLaunch: RocketLaunch) : RoomRocketLaunchAggregate {
            return RoomRocketLaunchAggregate(
                rocketLaunch = RoomRocketLaunch.fromDomain(rocketLaunch),
                rocket = RoomRocket.fromDomain(rocketLaunch.rocket)
            )
        }
    }

    fun toDomain() : RocketLaunch {
        return RocketLaunch(
            id = Id(rocketLaunch.id),
            missionName = rocketLaunch.missionName,
            launchDateTime = DateTimeDataMapper.fromString(rocketLaunch.launchDate),
            rocket = rocket.toDomain(),
            status = RoomRocketLaunchStatusMapper.toDomain(rocketLaunch.status),
            patchImageLink = rocketLaunch.patchImageLink?.let { Link(it) },
            articleLink = rocketLaunch.articleLink?.let { Link(it) },
            wikipediaLink = rocketLaunch.wikipediaLink?.let { Link(it) },
            videoLink = rocketLaunch.videoLink?.let { Link(it) }
        )
    }
}