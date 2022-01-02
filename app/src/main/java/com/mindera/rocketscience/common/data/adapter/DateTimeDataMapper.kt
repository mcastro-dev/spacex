package com.mindera.rocketscience.common.data.adapter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Source: https://developer.android.com/reference/kotlin/java/time/format/DateTimeFormatter
 */
object DateTimeDataMapper {

    fun toString(dateTime: LocalDateTime) : String {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    fun fromString(text: String) : LocalDateTime {
        return LocalDateTime.parse(text.replace("Z", ""))
    }
}