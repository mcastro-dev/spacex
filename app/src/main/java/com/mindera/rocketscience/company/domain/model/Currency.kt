package com.mindera.rocketscience.company.domain.model

/**
 * Must use ISO 4217 3-letter code.
 * https://en.wikipedia.org/wiki/ISO_4217
 */
enum class Currency(val isoCode: String) {
    USD("USD")
}