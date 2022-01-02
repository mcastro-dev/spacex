package com.mindera.rocketscience.rocketlaunch.domain.repository

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

interface IRocketLaunchRepository {
    suspend fun getRocketLaunches(forPage: Page, withFilter: Filter): Result<Data<List<RocketLaunch>>>
    suspend fun refreshRocketLaunches(withFilter: Filter): Result<Data<List<RocketLaunch>>>
    suspend fun getFilter(): Result<Filter>
    suspend fun saveFilter(filter: Filter): Result<Filter>
}