package com.mindera.rocketscience.company.data.local.sharedprefs.model

import com.mindera.rocketscience.company.domain.model.*

data class PrefsCompany(
    val name: String,
    val founder: String,
    val foundedYear: Int,
    val employeesCount: Int,
    val launchSitesCount: Int,
    val valuationInDollars: Long
) {
    companion object {
        fun fromDomain(company: Company): PrefsCompany {
            return PrefsCompany(
                name = company.name,
                founder = company.founder,
                foundedYear = company.foundedIn.value,
                employeesCount = company.employeesCount,
                launchSitesCount = company.launchSitesCount,
                valuationInDollars = Exchange(company.valuation).to(Currency.USD).value
            )
        }
    }

    fun toDomain(): Company {
        return Company(
            name = this.name,
            founder = this.founder,
            foundedIn = Year(this.foundedYear),
            employeesCount = this.employeesCount,
            launchSitesCount = this.launchSitesCount,
            valuation = Money(valuationInDollars)
        )
    }
}