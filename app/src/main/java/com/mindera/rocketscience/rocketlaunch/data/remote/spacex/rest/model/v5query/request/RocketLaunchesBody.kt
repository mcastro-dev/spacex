package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.request

import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.mapper.LaunchSuccessMapper
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper.SortOrderMapper
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter

object RocketLaunchesBodyJsonFactory {

    fun create(withFilter: Filter, offset: Int, limit: Int) : String {

        val launchYear = withFilter.rocketLaunchYear
        val sortOrder = SortOrderMapper.fromDomain(withFilter.sortOrder)
        val isLaunchSuccessful = LaunchSuccessMapper.fromDomain(withFilter.missionStatus)


        val queryIsLaunchSuccess = if (isLaunchSuccessful == null) {
            ""
        } else {
            ""","success": {"${"$"}eq": $isLaunchSuccessful}"""
        }

        return """{
            "options": {
                "select": {
                    "name": 1,
                    "success": 1,
                    "flight_number": 1,
                    "date_utc": 1,
                    "links.patch.small": 1,
                    "links.article": 1,
                    "links.wikipedia": 1,
                    "links.webcast": 1
                },
                "populate": [
                    {
                        "path": "rocket",
                        "select": {
                            "id": 1,
                            "name": 1,
                            "engines.type": 1
                        }
                    }
                ],
                "sort": {
                    "flight_number": "$sortOrder"
                },
                "offset": $offset,
                "limit": $limit
            },
            "query": {
                "date_utc": {
                    "${"$"}gte": "$launchYear"
                }
                $queryIsLaunchSuccess
            }
        }"""
    }
}