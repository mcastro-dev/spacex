package com.mindera.rocketscience.rocketlaunch.data.remote

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

interface IRemoteRocketLaunchDataSource {
    suspend fun getLaunches(forPage: Page, withFilter: Filter): List<RocketLaunch>
}