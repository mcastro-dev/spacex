package com.mindera.rocketscience.home.company.model

import com.mindera.rocketscience.company.domain.model.Money
import java.text.NumberFormat
import java.util.*

object MoneyFormatter {
    fun format(money: Money): String {
        val currencyFormatter = NumberFormat.getInstance().apply {
            currency = Currency.getInstance(money.currency.isoCode)
        }
        return "${money.currency.isoCode} ${currencyFormatter.format(money.value)}"
    }
}