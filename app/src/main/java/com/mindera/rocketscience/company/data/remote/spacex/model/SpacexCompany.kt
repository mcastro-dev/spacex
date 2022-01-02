package com.mindera.rocketscience.company.data.remote.spacex.model

import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.model.Money
import com.mindera.rocketscience.company.domain.model.Year

data class SpacexCompany(
    @SerializedName("name")
    val name : String,
    @SerializedName("founder")
    val founder: String,
    @SerializedName("founded")
    val foundedIn: Int,
    @SerializedName("employees")
    val employeesCount: Int,
    @SerializedName("launch_sites")
    val launchSitesCount: Int,
    @SerializedName("valuation")
    val valuation: Long
) {
    fun toDomain(): Company {
        return Company(
            this.name,
            this.founder,
            Year(this.foundedIn),
            this.employeesCount,
            this.launchSitesCount,
            Money(this.valuation)
        )
    }
}