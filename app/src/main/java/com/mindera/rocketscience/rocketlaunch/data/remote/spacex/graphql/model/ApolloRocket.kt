package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.graphql.model

import com.mindera.rocketscience.GetRocketLaunchesQuery
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket

fun GetRocketLaunchesQuery.Rocket.toDomain() : Rocket {
    return Rocket(
        id = Id(rocket?.id!!),
        name = rocket_name!!,
        type = rocket_type!!
    )
}