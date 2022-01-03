package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.source

import com.mindera.rocketscience.rocketlaunch.data.remote.IRemoteRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.request.RocketLaunchesBodyJsonFactory
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import javax.inject.Inject

class SpacexRocketLaunchRestDataSource @Inject constructor(
    private val restAPI: SpacexRocketLaunchRestAPI
) : IRemoteRocketLaunchDataSource {

    override suspend fun getLaunches(forPage: Page, withFilter: Filter): List<RocketLaunch> {
        val limit = forPage.expectedItemsPerPage
        val offset = forPage.number * limit

        val result = restAPI.getRocketLaunches(
            RocketLaunchesBodyJsonFactory.create(withFilter, offset, limit)
        )

        return result.documents.map { it.toDomain() }
    }
}
