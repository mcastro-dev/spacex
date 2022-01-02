package com.mindera.rocketscience.rocketlaunch.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mindera.rocketscience.common.data.adapter.DateTimeDataMapper
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.table.RocketLaunchTable
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.table.RocketTable
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

@Entity(
    tableName = RocketLaunchTable.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = RoomRocket::class,
        parentColumns = arrayOf(RocketTable.COLUMN_ID),
        childColumns = arrayOf(RocketLaunchTable.COLUMN_ROCKET_ID)
    )]
)
data class RoomRocketLaunch(
    @ColumnInfo(name = RocketLaunchTable.COLUMN_ID)
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_MISSION_NAME)
    val missionName: String,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_LAUNCH_YEAR)
    val launchYear: Int,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_LAUNCH_DATE)
    val launchDate: String,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_ROCKET_ID, index = true)
    val rocketId: String,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_STATUS)
    val status: String,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_PATCH_IMAGE_LINK)
    val patchImageLink: String?,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_ARTICLE_LINK)
    val articleLink: String?,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_WIKIPEDIA_LINK)
    val wikipediaLink: String?,
    @ColumnInfo(name = RocketLaunchTable.COLUMN_VIDEO_LINK)
    val videoLink: String?,
) {
    companion object {
        fun fromDomain(rocketLaunch: RocketLaunch) : RoomRocketLaunch {
            return RoomRocketLaunch(
                id = rocketLaunch.id.value,
                missionName = rocketLaunch.missionName,
                launchYear = rocketLaunch.launchDateTime.year,
                launchDate = DateTimeDataMapper.toString(rocketLaunch.launchDateTime),
                rocketId = rocketLaunch.rocket.id.value,
                status = RoomRocketLaunchStatusMapper.fromDomain(rocketLaunch.status),
                patchImageLink = rocketLaunch.patchImageLink?.value,
                articleLink = rocketLaunch.articleLink?.value,
                wikipediaLink = rocketLaunch.wikipediaLink?.value,
                videoLink = rocketLaunch.videoLink?.value
            )
        }
    }
}