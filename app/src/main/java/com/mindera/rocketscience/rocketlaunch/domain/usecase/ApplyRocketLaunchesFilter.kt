package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.rocketlaunch.domain.event.FiltersChanged
import com.mindera.rocketscience.rocketlaunch.domain.event.IRocketLaunchEventsPublisher
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import javax.inject.Inject

interface IApplyRocketLaunchesFilter : ICoroutineUseCase<ApplyRocketLaunchesFilter.Params, Result<Filter>>

class ApplyRocketLaunchesFilter @Inject constructor(
    private val repository: IRocketLaunchRepository,
    private val eventPublisher: IRocketLaunchEventsPublisher
) : IApplyRocketLaunchesFilter {

    override suspend fun invoke(params: Params): Result<Filter> {
        val result = repository.saveFilter(params.filter)

        if (result.isSuccess) {
            val filter = result.getOrNull()!!
            eventPublisher.publish(FiltersChanged(filter))
        }

        return result
    }

    data class Params(
        val filter: Filter
    )
}