package com.mindera.rocketscience.company.domain.model

/**
 * matheus: we could simply use integer instead of this class, but having a value object makes it easier to
 * implement domain logic in the future.
 */
data class Year(
    val value: Int
) {
    init {
        // matheus: domain validation logic, just as example
        assert(value > 0)
    }
}