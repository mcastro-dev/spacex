package com.mindera.rocketscience.rocketlaunch.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.rocketlaunch.data.error.*
import com.mindera.rocketscience.rocketlaunch.data.local.ILocalFilterDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.ILocalRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.remote.IRemoteRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.Page
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RocketLaunchRepository @Inject constructor(
    private val remoteDataSource: IRemoteRocketLaunchDataSource,
    private val localDataSource: ILocalRocketLaunchDataSource,
    private val localFilterDataSource: ILocalFilterDataSource
) : IRocketLaunchRepository {

    override suspend fun getRocketLaunches(forPage: Page, withFilter: Filter) = withContext(Dispatchers.IO) {
        val remoteLaunches: List<RocketLaunch>

        try {
            remoteLaunches = remoteDataSource.getLaunches(forPage, withFilter)

        } catch (remoteException: Exception) {
            return@withContext try {
                val localLaunches = localDataSource.getLaunches(forPage, withFilter)
                Result.success(Data(Data.State.STALE, localLaunches))

            } catch (e: Exception) {
                if (remoteException is SocketTimeoutException
                    || remoteException is UnknownHostException) {
                    Result.failure(UnreachableServer())
                } else {
                    throw RemoteAndLocalGetRocketLaunchesFailed(remoteException)
                }
            }
        }

        // Remote GET succeeded

        try {
            if (!remoteLaunches.isNullOrEmpty()) {
                localDataSource.saveLaunches(remoteLaunches)
            }
        } catch (e: Exception) {
            throw LocalSaveRocketLaunchesFailed(e)
        }

        try {
            val localLaunches = localDataSource.getLaunches(forPage, withFilter)
            return@withContext Result.success(Data(Data.State.FRESH, localLaunches))

        } catch (e: Exception) {
            throw LocalGetRocketLaunchesFailed(e)
        }
    }

    override suspend fun refreshRocketLaunches(withFilter: Filter) = withContext(Dispatchers.IO) {
        val page = Page.first()
        val remoteRocketLaunches: List<RocketLaunch>

        try {
            remoteRocketLaunches = remoteDataSource.getLaunches(forPage = page, withFilter)

        } catch (e: SocketTimeoutException) {
            return@withContext Result.failure(UnreachableServer())

        } catch (e: UnknownHostException) {
            return@withContext Result.failure(UnreachableServer())

        } catch (e: Exception) {
            throw RemoteGetRocketLaunchesFailed(e)
        }

        // Remote GET succeeded

        try {
            localDataSource.deleteAllRocketLaunches()
        } catch (e: Exception) {
            return@withContext Result.failure(LocalDeleteAllRocketLaunchesFailed())
        }

        try {
            localDataSource.deleteAllRockets()
        } catch (e: Exception) {
            throw LocalDeleteAllRocketsFailed(e)
        }

        if (remoteRocketLaunches.isEmpty()) {
            return@withContext Result.success(Data(Data.State.FRESH, emptyList()))
        }

        try {
            localDataSource.saveLaunches(remoteRocketLaunches)
        } catch (e: Exception) {
            throw LocalSaveRocketLaunchesFailed(e)
        }

        try {
            val localLaunches = localDataSource.getLaunches(forPage = page, withFilter)
            return@withContext Result.success(Data(Data.State.FRESH, localLaunches))

        } catch (e: Exception) {
            throw LocalGetRocketLaunchesFailed(e)
        }
    }

    override suspend fun getFilter(): Result<Filter> = withContext(Dispatchers.IO) {
        return@withContext try {
            val filter = localFilterDataSource.getFilter()
            Result.success(filter)

        } catch (e: Exception) {
            throw GetFilterFailed(e)
        }
    }

    override suspend fun saveFilter(filter: Filter): Result<Filter> = withContext(Dispatchers.IO){
        return@withContext try {
            localFilterDataSource.saveFilter(filter)
            Result.success(filter)

        } catch (e: Exception) {
            throw SaveFilterFailed(e)
        }
    }
}