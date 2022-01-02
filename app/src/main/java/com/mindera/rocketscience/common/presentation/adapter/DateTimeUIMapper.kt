package com.mindera.rocketscience.common.presentation.adapter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Source: https://developer.android.com/reference/kotlin/java/time/format/DateTimeFormatter
 */
object DateTimeUIMapper {
    fun toString(dateTime: LocalDateTime) : String {
        return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    }
}