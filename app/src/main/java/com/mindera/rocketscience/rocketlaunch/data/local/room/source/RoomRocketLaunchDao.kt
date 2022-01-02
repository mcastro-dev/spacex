package com.mindera.rocketscience.rocketlaunch.data.local.room.source

import androidx.room.*
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocket
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocketLaunch
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocketLaunchAggregate

@Dao
interface RoomRocketLaunchDao {

    @Transaction
    @Query("SELECT * FROM RocketLaunch WHERE launchYear >= :minLaunchYear ORDER BY launchDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getLaunchesDescending(offset: Int, limit: Int, minLaunchYear: Int): List<RoomRocketLaunchAggregate>

    @Transaction
    @Query("SELECT * FROM RocketLaunch WHERE launchYear >= :minLaunchYear AND status = :status ORDER BY launchDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getLaunchesDescending(offset: Int, limit: Int, minLaunchYear: Int, status: String): List<RoomRocketLaunchAggregate>

    @Transaction
    @Query("SELECT * FROM RocketLaunch WHERE launchYear >= :minLaunchYear ORDER BY launchDate ASC LIMIT :limit OFFSET :offset")
    suspend fun getLaunchesAscending(offset: Int, limit: Int, minLaunchYear: Int): List<RoomRocketLaunchAggregate>

    @Transaction
    @Query("SELECT * FROM RocketLaunch WHERE launchYear >= :minLaunchYear AND status = :status ORDER BY launchDate ASC LIMIT :limit OFFSET :offset")
    suspend fun getLaunchesAscending(offset: Int, limit: Int, minLaunchYear: Int, status: String): List<RoomRocketLaunchAggregate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<RoomRocketLaunch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<RoomRocket>)

    @Query("DELETE FROM RocketLaunch")
    suspend fun deleteAllLaunches() : Int

    @Query("DELETE FROM Rocket")
    suspend fun deleteAllRockets() : Int
}