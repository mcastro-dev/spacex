package com.mindera.rocketscience.rocketlaunch.domain.event

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter

data class FiltersChanged(
    val newFilter: Filter
) : IRocketLaunchEvent