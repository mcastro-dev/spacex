package com.mindera.rocketscience.home.itself.viewmodel

import com.mindera.rocketscience.common.presentation.viewmodel.UIState
import com.mindera.rocketscience.home.company.model.UICompany
import com.mindera.rocketscience.home.rocketlaunch.itself.model.UIRocketLaunch

data class HomeState(
    val isRefreshing: Boolean = false,
    val isLoadingCompanyInfo: Boolean = false,
    val isLoadingRocketLaunches: Boolean = false,
    val hasStaleData: Boolean = false,
    val company: UICompany? = null,
    val rocketLaunches: List<UIRocketLaunch> = emptyList()
) : UIState