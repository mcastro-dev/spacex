package com.mindera.rocketscience.rocketlaunch.data.local.sharedprefs.model

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder

data class PrefsFilter(
    val rocketLaunchYear: Int = Filter().rocketLaunchYear,
    val sortOrder: String = Filter().sortOrder.name,
    val launchStatus: String = Filter().missionStatus.name
) {
    companion object {
        fun fromDomain(filter: Filter): PrefsFilter {
            return PrefsFilter(
                rocketLaunchYear = filter.rocketLaunchYear,
                sortOrder = filter.sortOrder.name,
                launchStatus = filter.missionStatus.name
            )
        }
    }

    fun toDomain(): Filter {
        return Filter(
            rocketLaunchYear = rocketLaunchYear,
            sortOrder = SortOrder.valueOf(sortOrder),
            missionStatus = Filter.MissionStatus.valueOf(launchStatus)
        )
    }
}