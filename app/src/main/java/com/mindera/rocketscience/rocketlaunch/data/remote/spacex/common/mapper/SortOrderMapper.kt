package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.common.mapper

import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder

object SortOrderMapper {
    fun fromDomain(order: SortOrder) : String {
        return when(order) {
            SortOrder.ASCENDING -> "asc"
            SortOrder.DESCENDING -> "desc"
        }
    }
}