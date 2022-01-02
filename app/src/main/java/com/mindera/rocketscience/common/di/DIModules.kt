package com.mindera.rocketscience.common.di

import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.mindera.rocketscience.common.data.local.AppRoomDatabase
import com.mindera.rocketscience.common.data.local.LocalDataSourceConstants
import com.mindera.rocketscience.common.data.remote.RemoteDataSourceConstants
import com.mindera.rocketscience.common.data.service.AndroidConnectivityService
import com.mindera.rocketscience.common.data.service.INetworkConnectivityService
import com.mindera.rocketscience.company.data.CompanyRepository
import com.mindera.rocketscience.company.data.local.ILocalCompanyDataSource
import com.mindera.rocketscience.company.data.local.sharedprefs.source.CompanySharedPrefsDataSource
import com.mindera.rocketscience.company.data.remote.IRemoteCompanyDataSource
import com.mindera.rocketscience.company.data.remote.spacex.source.SpacexCompanyRestAPI
import com.mindera.rocketscience.company.data.remote.spacex.source.SpacexCompanyRestDataSource
import com.mindera.rocketscience.company.domain.repository.ICompanyRepository
import com.mindera.rocketscience.company.domain.usecase.GetCompanyInfo
import com.mindera.rocketscience.company.domain.usecase.IGetCompanyInfo
import com.mindera.rocketscience.company.domain.usecase.IRefreshCompanyInfo
import com.mindera.rocketscience.company.domain.usecase.RefreshCompanyInfo
import com.mindera.rocketscience.home.itself.domain.usecase.IRefreshHome
import com.mindera.rocketscience.home.itself.domain.usecase.RefreshHome
import com.mindera.rocketscience.rocketlaunch.data.RocketLaunchRepository
import com.mindera.rocketscience.rocketlaunch.data.local.ILocalFilterDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.ILocalRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.room.source.RoomRocketLaunchDao
import com.mindera.rocketscience.rocketlaunch.data.local.room.source.RoomRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.local.sharedprefs.source.FilterSharedPrefsDataSource
import com.mindera.rocketscience.rocketlaunch.data.remote.IRemoteRocketLaunchDataSource
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.source.SpacexRocketLaunchRestAPI
import com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.source.SpacexRocketLaunchRestDataSource
import com.mindera.rocketscience.rocketlaunch.domain.event.IRocketLaunchEventsPublisher
import com.mindera.rocketscience.rocketlaunch.domain.event.RocketLaunchEventsPublisher
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import com.mindera.rocketscience.rocketlaunch.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedDIModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCompanyRepository(repository: CompanyRepository) : ICompanyRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindRocketLaunchRepository(repository: RocketLaunchRepository) : IRocketLaunchRepository

    @Binds
    abstract fun bindGetRocketLaunchesUseCase(useCase: GetRocketLaunches) : IGetRocketLaunches

    @Binds
    abstract fun bindGetCompanyInfoUseCase(useCase: GetCompanyInfo) : IGetCompanyInfo

    @Binds
    abstract fun bindRefreshCompanyInfoUseCase(useCase: RefreshCompanyInfo) : IRefreshCompanyInfo

    @Binds
    abstract fun bindRefreshRocketLaunchesUseCase(useCase: RefreshRocketLaunches) : IRefreshRocketLaunches

    @Binds
    abstract fun bindGetRocketLaunchesFilterUseCase(useCase: GetRocketLaunchesFilter) : IGetRocketLaunchesFilter

    @Binds
    abstract fun bindApplyRocketLaunchesFilterUseCase(useCase: ApplyRocketLaunchesFilter) : IApplyRocketLaunchesFilter

    @Binds
    abstract fun bindRefreshHomeUseCase(useCase: RefreshHome) : IRefreshHome
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonDIModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DispatchersIO

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DispatchersMain

    @Binds
    abstract fun bindCompanyRemoteDataSource(dataSource: SpacexCompanyRestDataSource) : IRemoteCompanyDataSource

    @Binds
    abstract fun bindRocketLaunchRemoteDataSource(dataSource: SpacexRocketLaunchRestDataSource) : IRemoteRocketLaunchDataSource

    @Binds
    abstract fun bindLaunchLocalDataSource(dataSource: RoomRocketLaunchDataSource): ILocalRocketLaunchDataSource

    companion object {

        @Provides
        fun provideRetrofit(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(RemoteDataSourceConstants.REST_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        }

        @Provides
        fun provideApolloClient(): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl(RemoteDataSourceConstants.GRAPH_QL_URL)
                .build()
        }

        @Provides
        @Singleton
        fun provideCompanyRestAPI(retrofitBuilder: Retrofit.Builder): SpacexCompanyRestAPI {
            return retrofitBuilder.build().create(SpacexCompanyRestAPI::class.java)
        }

        @Provides
        @Singleton
        fun provideLaunchesRestAPI(retrofitBuilder: Retrofit.Builder): SpacexRocketLaunchRestAPI {
            return retrofitBuilder.build().create(SpacexRocketLaunchRestAPI::class.java)
        }

        @Provides
        fun provideCompanyLocalDataSource(
            @ApplicationContext context: Context
        ): ILocalCompanyDataSource = CompanySharedPrefsDataSource(context = context)

        @Provides
        fun provideLocalFilterDataSource(
            @ApplicationContext context: Context
        ): ILocalFilterDataSource = FilterSharedPrefsDataSource(context = context)

        @Provides
        @Singleton
        fun provideLocalDao(database: AppRoomDatabase) : RoomRocketLaunchDao = database.rocketLaunchDao()

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): AppRoomDatabase = Room.databaseBuilder(
            context,
            AppRoomDatabase::class.java,
            LocalDataSourceConstants.DB_FILE_NAME
        ).build()

        @Provides
        @Singleton
        fun provideRocketLaunchEventsPublisher() : IRocketLaunchEventsPublisher {
            return RocketLaunchEventsPublisher(
                eventsSubject = PublishSubject.create(),
            )
        }

        @DispatchersIO
        @Provides
        fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO

        @DispatchersMain
        @Provides
        fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

        @Provides
        fun provideNetworkConnectivityService(
            @ApplicationContext context: Context
        ) : INetworkConnectivityService = AndroidConnectivityService(context)
    }
}