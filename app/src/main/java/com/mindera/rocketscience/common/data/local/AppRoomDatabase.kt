package com.mindera.rocketscience.common.data.local

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocket
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.RoomRocketLaunch
import com.mindera.rocketscience.rocketlaunch.data.local.room.source.RoomRocketLaunchDao

@Database(
    entities = [RoomRocketLaunch::class, RoomRocket::class],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun rocketLaunchDao(): RoomRocketLaunchDao
}