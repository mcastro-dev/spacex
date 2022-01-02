package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.rocketlaunch.domain.error.UnableToGetFilters
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import javax.inject.Inject

interface IRefreshRocketLaunches : ICoroutineUseCase<Unit, Result<Data<List<RocketLaunch>>>>

class RefreshRocketLaunches @Inject constructor(
    private val repository: IRocketLaunchRepository
) : IRefreshRocketLaunches {

    override suspend fun invoke(params: Unit): Result<Data<List<RocketLaunch>>> {
        val filterResult = repository.getFilter()
        if (filterResult.isFailure) {
            return Result.failure(UnableToGetFilters())
        }
        val filter = filterResult.getOrNull()!!

        return repository.refreshRocketLaunches(withFilter = filter)
    }
}