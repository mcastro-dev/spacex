package com.mindera.rocketscience.company.domain.model

data class Company(
    val name: String,
    val founder: String,
    val foundedIn: Year,
    val employeesCount: Int,
    val launchSitesCount: Int,
    val valuation: Money
)