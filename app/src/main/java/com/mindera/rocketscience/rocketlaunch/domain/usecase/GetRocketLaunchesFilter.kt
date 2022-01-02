package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import javax.inject.Inject

interface IGetRocketLaunchesFilter : ICoroutineUseCase<Unit, Result<Filter>>

class GetRocketLaunchesFilter @Inject constructor(
    private val repository: IRocketLaunchRepository
) : IGetRocketLaunchesFilter {

    override suspend fun invoke(params: Unit): Result<Filter> {
        return repository.getFilter()
    }
}