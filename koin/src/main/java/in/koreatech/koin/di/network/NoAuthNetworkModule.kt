package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.core.qualifier.NoAuth
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NoAuthNetworkModule {
    @NoAuth
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @NoAuth
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @ServerUrl baseUrl: String,
        @NoAuth okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /* Auth retrofit instances below */
    @Provides
    @Singleton
    fun provideUserApi(
        @NoAuth retrofit: Retrofit
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOwnerApi(
        @NoAuth retrofit: Retrofit
    ): OwnerApi {
        return retrofit.create(OwnerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVersionApi(
        @NoAuth retrofit: Retrofit
    ): VersionApi {
        return retrofit.create(VersionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeptApi(
        @NoAuth retrofit: Retrofit
    ): DeptApi {
        return retrofit.create(DeptApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDiningApi(
        @NoAuth retrofit: Retrofit
    ): DiningApi {
        return retrofit.create(DiningApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBusApi(
        @NoAuth retrofit: Retrofit
    ): BusApi{
        return retrofit.create(BusApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreApi(
        @NoAuth retrofit: Retrofit
    ): StoreApi {
        return retrofit.create(StoreApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideLandApi(
        @NoAuth retrofit: Retrofit
    ): LandApi {
        return retrofit.create(LandApi::class.java)
    }
}