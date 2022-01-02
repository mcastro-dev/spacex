package com.mindera.rocketscience.common.domain.model

data class Data<T>(
    val state: State,
    val value: T
) {
    enum class State {
        STALE,
        FRESH
    }
}