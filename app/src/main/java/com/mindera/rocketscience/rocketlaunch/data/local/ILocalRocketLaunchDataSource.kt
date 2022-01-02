package com.mindera.rocketscience.rocketlaunch.data.local

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

interface ILocalRocketLaunchDataSource {
    suspend fun saveLaunches(launches: List<RocketLaunch>)
    suspend fun getLaunches(forPage: Page, withFilter: Filter): List<RocketLaunch>
    suspend fun deleteAllRocketLaunches()
    suspend fun deleteAllRockets()
}