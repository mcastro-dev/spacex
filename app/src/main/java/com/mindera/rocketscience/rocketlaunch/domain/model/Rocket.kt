package com.mindera.rocketscience.rocketlaunch.domain.model

import com.mindera.rocketscience.common.domain.model.Id

data class Rocket(
    val id: Id,
    val name: String,
    // matheus: should probably be an enum. Will do it if I have the time.
    val type: String
)