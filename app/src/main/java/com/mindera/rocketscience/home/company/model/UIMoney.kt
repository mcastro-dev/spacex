package com.mindera.rocketscience.home.company.model

import com.mindera.rocketscience.company.domain.model.Money

data class UIMoney(
    val value: String
) {
    companion object {
        fun fromDomain(money: Money): UIMoney {
            return UIMoney(MoneyFormatter.format(money))
        }
    }
}