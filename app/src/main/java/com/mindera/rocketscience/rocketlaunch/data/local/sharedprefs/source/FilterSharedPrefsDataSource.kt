package com.mindera.rocketscience.rocketlaunch.data.local.sharedprefs.source

import android.content.Context
import com.mindera.rocketscience.common.data.error.LocalDataNotFound
import com.mindera.rocketscience.rocketlaunch.data.local.ILocalFilterDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.sharedprefs.model.*
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilterSharedPrefsDataSource @Inject constructor(
    @ApplicationContext context: Context
) : ILocalFilterDataSource {

    companion object {
        private const val SHARED_PREFS_NAME = "ROCKET_LAUNCH_FILTER_PREFS"
    }

    private val defaultFilter = PrefsFilter()
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun saveFilter(filter: Filter) {
        with(sharedPrefs.edit()) {
            val prefsFilter = PrefsFilter.fromDomain(filter)

            putInt(KEY_LAUNCH_YEAR, prefsFilter.rocketLaunchYear)
            putString(KEY_SORT_ORDER, prefsFilter.sortOrder)
            putString(KEY_LAUNCH_STATUS, prefsFilter.launchStatus)

            commit()
        }
    }

    override suspend fun getFilter(): Filter {
        val launchYear = sharedPrefs.getInt(KEY_LAUNCH_YEAR, defaultFilter.rocketLaunchYear)
        val sortOrder = sharedPrefs.getString(KEY_SORT_ORDER, defaultFilter.sortOrder).orEmpty()
        val launchStatus = sharedPrefs.getString(KEY_LAUNCH_STATUS, defaultFilter.launchStatus).orEmpty()

        if (sortOrder.isEmpty() || launchStatus.isEmpty()) {
            throw LocalDataNotFound()
        }

        return PrefsFilter(
            rocketLaunchYear = launchYear,
            sortOrder = sortOrder,
            launchStatus = launchStatus
        ).toDomain()
    }
}