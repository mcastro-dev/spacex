package com.mindera.rocketscience.home.rocketlaunch.filter.viewmodel

import com.mindera.rocketscience.common.presentation.viewmodel.UIEvent
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder

class GetFiltersEvent : UIEvent
class ApplyFiltersEvent : UIEvent
class NextYearEvent : UIEvent
class PreviousYearEvent : UIEvent

// mathues: Should have created a UI model for these, but oh well, it's ok for now
data class ChangeMissionStatusEvent(val missionStatus: Filter.MissionStatus) : UIEvent
data class ChangeSortOrderEvent(val sortOrder: SortOrder) : UIEvent