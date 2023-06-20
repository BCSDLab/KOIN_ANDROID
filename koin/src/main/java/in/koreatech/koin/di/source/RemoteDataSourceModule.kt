package `in`.koreatech.koin.di.source

import `in`.koreatech.koin.data.api.*
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.remote.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        userApi: UserApi,
        userAuthApi: UserAuthApi
    ) : UserRemoteDataSource {
        return UserRemoteDataSource(userApi, userAuthApi)
    }

    @Provides
    @Singleton
    fun provideVersionRemoteDataSource(
        versionApi: VersionApi
    ) : VersionRemoteDataSource {
        return VersionRemoteDataSource(versionApi)
    }

    @Provides
    @Singleton
    fun provideDeptRemoteDataSource(
        deptApi: DeptApi
    ) : DeptRemoteDataSource {
        return DeptRemoteDataSource(deptApi)
    }

    @Provides
    @Singleton
    fun provideDiningRemoteDataSource(
        diningApi: DiningApi
    ): DiningRemoteDataSource {
        return DiningRemoteDataSource(diningApi)
    }

    @Provides
    @Singleton
    fun provideBusRemoteDataSource(
        busApi: BusApi
    ): BusRemoteDataSource {
        return BusRemoteDataSource(busApi)
    }

    @Provides
    @Singleton
    fun provideStoreRemoteDataSource(
        storeApi: StoreApi
    ): StoreRemoteDataSource {
        return StoreRemoteDataSource(storeApi)
    }
    
    @Provides
    @Singleton
    fun provideLandRemoteDataSource(
        landApi: LandApi
    ): LandRemoteDataSource {
        return LandRemoteDataSource(landApi)
    }
}