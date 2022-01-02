package com.mindera.rocketscience.company.domain.model

data class Money(
    val value: Long,
    val currency: Currency = Currency.USD
)