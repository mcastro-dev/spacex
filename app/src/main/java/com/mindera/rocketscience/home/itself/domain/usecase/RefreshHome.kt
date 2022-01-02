package com.mindera.rocketscience.home.itself.domain.usecase

import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.common.domain.usecase.ICoroutineUseCase
import com.mindera.rocketscience.company.domain.usecase.IRefreshCompanyInfo
import com.mindera.rocketscience.home.itself.domain.model.Home
import com.mindera.rocketscience.rocketlaunch.domain.usecase.IRefreshRocketLaunches
import javax.inject.Inject

interface IRefreshHome : ICoroutineUseCase<Unit, Result<Data<Home>>>

class RefreshHome @Inject constructor(
    private val refreshRocketLaunches: IRefreshRocketLaunches,
    private val refreshCompanyInfo: IRefreshCompanyInfo
) : IRefreshHome {

    override suspend fun invoke(params: Unit): Result<Data<Home>> {

        val companyInfoResult = refreshCompanyInfo(Unit)
        if (companyInfoResult.isFailure) {
            return Result.failure(companyInfoResult.exceptionOrNull()!!)
        }

        val rocketLaunchesResult = refreshRocketLaunches(Unit)
        if (rocketLaunchesResult.isFailure) {
            return Result.failure(rocketLaunchesResult.exceptionOrNull()!!)
        }

        val rocketLaunchesData = rocketLaunchesResult.getOrNull()!!
        val rocketLaunches = rocketLaunchesData.value
        val dataState = rocketLaunchesData.state

        val home = Home(
            company = companyInfoResult.getOrNull(),
            rocketLaunches = rocketLaunches
        )
        return Result.success(Data(dataState, home))
    }
}