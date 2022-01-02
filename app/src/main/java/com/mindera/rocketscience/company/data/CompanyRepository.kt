package com.mindera.rocketscience.company.data

import com.mindera.rocketscience.common.domain.error.UnreachableServer
import com.mindera.rocketscience.common.domain.model.Data
import com.mindera.rocketscience.company.data.error.*
import com.mindera.rocketscience.company.data.local.ILocalCompanyDataSource
import com.mindera.rocketscience.company.data.remote.IRemoteCompanyDataSource
import com.mindera.rocketscience.company.domain.model.Company
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val remoteDataSource: IRemoteCompanyDataSource,
    private val localDataSource: ILocalCompanyDataSource
) : ICompanyRepository {

    override suspend fun getInfo(): Result<Data<Company>> = withContext(Dispatchers.IO) {
        val remoteInfo: Company

        try {
            remoteInfo = remoteDataSource.getInfo()

        } catch (remoteException: Exception) {
            return@withContext try {
                val localInfo = localDataSource.getInfo()
                Result.success(Data(Data.State.STALE, localInfo))

            } catch (e: Exception) {
                if (remoteException is SocketTimeoutException
                    || remoteException is UnknownHostException) {
                    Result.failure(UnreachableServer())
                } else {
                    throw RemoteAndLocalGetCompanyInfoFailed(remoteException)
                }
            }
        }

        try {
            localDataSource.saveInfo(remoteInfo)
        } catch (e: Exception) {
            throw LocalSaveCompanyInfoFailed(e)
        }

        return@withContext Result.success(Data(Data.State.FRESH, remoteInfo))
    }

    override suspend fun forceRefresh() = withContext(Dispatchers.IO) {
        val remoteInfo: Company

        try {
            remoteInfo = remoteDataSource.getInfo()

        } catch (e: SocketTimeoutException) {
            return@withContext Result.failure(UnreachableServer())

        } catch (e: UnknownHostException) {
            return@withContext Result.failure(UnreachableServer())

        } catch (e: Exception) {
            throw RemoteGetCompanyInfoFailed(e)
        }

        // Remote GET succeeded
        try {
            localDataSource.deleteInfo()
        } catch (e: Exception) {
            throw LocalDeleteCompanyInfoFailed(e)
        }

        try {
            localDataSource.saveInfo(remoteInfo)
            return@withContext Result.success(remoteInfo)

        } catch (e: Exception) {
            throw LocalSaveCompanyInfoFailed(e)
        }
    }
}