package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.rocketlaunch.domain.error.UnableToGetFilters
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import javax.inject.Inject

interface IGetRocketLaunches : ICoroutineUseCase<GetRocketLaunches.Params, Result<Data<List<RocketLaunch>>>>

class GetRocketLaunches @Inject constructor(
    private val repository: IRocketLaunchRepository
) : IGetRocketLaunches {

    override suspend fun invoke(params: Params): Result<Data<List<RocketLaunch>>> {
        val filterResult = repository.getFilter()
        if (filterResult.isFailure) {
            return Result.failure(UnableToGetFilters())
        }

        val filter = filterResult.getOrNull()!!

        return repository.getRocketLaunches(forPage = params.page, withFilter = filter)
    }

    data class Params(
        val page: Page
    )
}