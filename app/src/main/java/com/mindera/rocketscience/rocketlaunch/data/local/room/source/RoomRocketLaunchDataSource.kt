package com.mindera.rocketscience.rocketlaunch.data.local.room.source

import com.mindera.rocketscience.rocketlaunch.data.local.ILocalRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocket
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocketLaunch
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocketLaunchStatusMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.model.mapper.LaunchStatusMapper
import javax.inject.Inject

class RoomRocketLaunchDataSource @Inject constructor(
    private val dao: RoomRocketLaunchDao
) : ILocalRocketLaunchDataSource {

    override suspend fun saveLaunches(launches: List<RocketLaunch>) {
        val rocketLaunchesDTO = launches.map { RoomRocketLaunch.fromDomain(it) }
        val rocketsDTO = launches.map { RoomRocket.fromDomain(it.rocket) }

        // FIXME: matheus: should ideally run in a single transaction performing rollback in case something goes wrong,
        //  but unfortunately Room is giving me ForeignKey error at runtime while doing that.
        //  Gonna try fixing it if there's enough time.
        dao.insertRockets(rocketsDTO)
        dao.insertLaunches(rocketLaunchesDTO)
    }

    // IMPORTANT: matheus: apparently there's no way to make Room recognize a parameter as the ASC or DESC keyword,
    //  therefore had to implement this functionality by using multiple "similar" methods:
    override suspend fun getLaunches(forPage: Page, withFilter: Filter): List<RocketLaunch> {
        val limit = forPage.expectedItemsPerPage
        val offset = forPage.number * limit
        val minLaunchYear = withFilter.rocketLaunchYear
        val sortOrder = withFilter.sortOrder
        val missionStatus = withFilter.missionStatus

        val result = if (missionStatus == Filter.MissionStatus.ANY) {
            when(sortOrder) {
                SortOrder.ASCENDING -> dao.getLaunchesAscending(offset = offset, limit = limit, minLaunchYear = minLaunchYear)
                SortOrder.DESCENDING -> dao.getLaunchesDescending(offset = offset, limit = limit, minLaunchYear = minLaunchYear)
            }
        } else {
            val rocketLaunchStatus = RoomRocketLaunchStatusMapper.fromDomain(
                LaunchStatusMapper.fromFilterMissionStatus(missionStatus)
            )

            when(sortOrder) {
                SortOrder.ASCENDING -> dao.getLaunchesAscending(
                    offset = offset,
                    limit = limit,
                    minLaunchYear = minLaunchYear,
                    status = rocketLaunchStatus)

                SortOrder.DESCENDING -> dao.getLaunchesDescending(
                    offset = offset,
                    limit = limit,
                    minLaunchYear = minLaunchYear,
                    status = rocketLaunchStatus)
            }
        }

        return result.map { it.toDomain() }
    }

    override suspend fun deleteAllRocketLaunches() {
        dao.deleteAllLaunches()
    }

    override suspend fun deleteAllRockets() {
        dao.deleteAllRockets()
    }
}