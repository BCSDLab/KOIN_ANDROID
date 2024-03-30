package `in`.koreatech.business.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.NoAuth
import `in`.koreatech.koin.data.api.OwnerApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoAuthNetworkModule {

    @Provides
    @Singleton
    fun provideOwnerApi(
        @NoAuth retrofit: Retrofit
    ): OwnerApi {
        return retrofit.create(OwnerApi::class.java)
    }
}