package com.mindera.rocketscience.home.itself.domain.model

import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

data class Home(
    val company: Company?,
    val rocketLaunches: List<RocketLaunch>
)