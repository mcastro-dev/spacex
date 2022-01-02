package com.mindera.rocketscience.home.company.model

import com.mindera.rocketscience.company.domain.model.Company

data class UICompany(
    val name: String,
    val founder: String,
    val foundedYear: Int,
    val employeesCount: Int,
    val launchSitesCount: Int,
    val valuation: UIMoney
) {
    companion object {
        fun fromDomain(company: Company) : UICompany {
            return UICompany(
                company.name,
                company.founder,
                company.foundedIn.value,
                company.employeesCount,
                company.launchSitesCount,
                UIMoney.fromDomain(company.valuation)
            )
        }
    }
}