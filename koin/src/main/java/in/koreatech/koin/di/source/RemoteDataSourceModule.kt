package `in`.koreatech.koin.di.source

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
}