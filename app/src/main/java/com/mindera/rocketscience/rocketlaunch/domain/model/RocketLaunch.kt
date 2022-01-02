package com.mindera.rocketscience.rocketlaunch.domain.model

import com.mindera.rocketscience.common.domain.model.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class RocketLaunch(
    val id: Id,
    val missionName: String,
    val launchDateTime: LocalDateTime,
    val rocket: Rocket,
    val status: Status,
    val patchImageLink: Link? = null,
    val articleLink: Link? = null,
    val wikipediaLink: Link? = null,
    val videoLink: Link? = null
) {

    /**
     * @return is a NEGATIVE number if the launch date is in the past,
     *  and is a POSITIVE number if the launch date is in the future.
     */
    val daysDiffFromLaunchToToday: Long get() {
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, launchDateTime.toLocalDate())
    }

    enum class Status {
        TO_BE_DONE,
        SUCCESS,
        FAILURE
    }
}