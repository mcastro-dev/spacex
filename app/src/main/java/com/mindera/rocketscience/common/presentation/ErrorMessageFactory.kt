package com.mindera.rocketscience.common.presentation

import android.content.Context
import com.mindera.rocketscience.R
import com.mindera.rocketscience.common.domain.error.NoInternetAccess
import com.mindera.rocketscience.common.domain.error.ServerDown
import com.mindera.rocketscience.company.data.error.*
import com.mindera.rocketscience.rocketlaunch.data.error.LocalDeleteAllRocketLaunchesFailed
import com.mindera.rocketscience.rocketlaunch.data.error.LocalDeleteAllRocketsFailed
import com.mindera.rocketscience.rocketlaunch.data.error.LocalSaveRocketLaunchesFailed
import com.mindera.rocketscience.rocketlaunch.data.error.RemoteAndLocalGetRocketLaunchesFailed

object ErrorMessageFactory {
    fun build(forError: Throwable, withContext: Context) : String {
        return when(forError) {
            is ServerDown -> withContext.getString(R.string.error_server_down)
            is NoInternetAccess -> withContext.getString(R.string.error_no_internet_access)
            else -> withContext.getString(R.string.error_default)
        }
    }
}