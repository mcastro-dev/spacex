package com.mindera.rocketscience.common.presentation.viewmodel

data class ShowErrorCommand(
    val error: Throwable
) : VMCommand