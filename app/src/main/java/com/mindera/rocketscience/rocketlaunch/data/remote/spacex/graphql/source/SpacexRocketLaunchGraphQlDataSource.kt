package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.mindera.rocketscience.GetRocketLaunchesQuery
import com.mindera.rocketscience.rocketlaunch.data.remote.IRemoteRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.model.LaunchSuccessMapper
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.model.toDomain
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper.SortOrderMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.type.LaunchFind
import javax.inject.Inject

class SpacexRocketLaunchGraphQlDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) : IRemoteRocketLaunchDataSource {

    override suspend fun getLaunches(forPage: Page, withFilter: Filter): List<RocketLaunch> {
        val limit = forPage.expectedItemsPerPage
        val offset = forPage.number * limit
        val order = SortOrderMapper.fromDomain(withFilter.sortOrder)
        val launchYear = withFilter.rocketLaunchYear.toString()
        val isLaunchSuccessful = LaunchSuccessMapper.fromDomain(withFilter.missionStatus)

        val result = apolloClient.query(
            GetRocketLaunchesQuery(
                offset = offset,
                limit = limit,
                order = order,
                find = LaunchFind(
                    launch_year = Optional.presentIfNotNull(launchYear),
                    launch_success = Optional.presentIfNotNull(isLaunchSuccessful)
                )
            )
        ).execute()

        return result.data!!.launches!!.map { it!!.toDomain() }
    }
}