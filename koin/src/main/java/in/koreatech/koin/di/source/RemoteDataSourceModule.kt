package `in`.koreatech.koin.di.source

import `in`.koreatech.koin.data.api.DeptApi
import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.VersionApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.remote.DeptRemoteDataSource
import `in`.koreatech.koin.data.source.remote.DiningRemoteDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.source.remote.VersionRemoteDataSource
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
}