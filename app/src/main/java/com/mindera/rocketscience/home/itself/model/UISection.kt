package com.mindera.rocketscience.home.itself.model

data class UISection(
    val title: String,
    val isSectionLoading: Boolean = false,
    val isEmpty: Boolean = true,
    val messageWhenEmpty: String? = null,
) {
    enum class Type {
        COMPANY,
        ROCKET_LAUNCHES
    }
}