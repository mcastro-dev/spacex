package com.mindera.rocketscience.rocketlaunch.data.local

import com.mindera.rocketscience.rocketlaunch.domain.model.Filter

interface ILocalFilterDataSource {
    suspend fun saveFilter(filter: Filter)
    suspend fun getFilter(): Filter
}