package com.mindera.rocketscience.rocketlaunch.domain.model

data class Filter(
    val rocketLaunchYear: Int = 2006,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val missionStatus: MissionStatus = MissionStatus.ANY
) {
    fun nextYear() : Filter {
        return this.copy(rocketLaunchYear = rocketLaunchYear + 1)
    }

    fun previousYear() : Filter {
        // matheus: could use some domain validation logic here (to prevent negative years, or something like that)
        return this.copy(rocketLaunchYear = rocketLaunchYear - 1)
    }

    enum class MissionStatus {
        ANY,
        SUCCESS,
        FAILURE
    }
}