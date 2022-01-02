package com.mindera.rocketscience.company.domain.model

class Exchange(private val money: Money) {
    fun to(currency: Currency): Money {
        if (money.currency == currency) {
            return money
        }
        TODO("Implement exchange")
    }
}