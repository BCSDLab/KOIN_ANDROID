package `in`.koreatech.koin.data.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.NoAuth
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.core.qualifier.UserAgent
import `in`.koreatech.koin.data.api.ArticleApi
import `in`.koreatech.koin.data.api.BannerApi
import `in`.koreatech.koin.data.api.BusApi
import `in`.koreatech.koin.data.api.ChatApi
import `in`.koreatech.koin.data.api.ClubApi
import `in`.koreatech.koin.data.api.CoopShopApi
import `in`.koreatech.koin.data.api.DeptApi
import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.api.LandApi
import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.VersionApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NoAuthNetworkModule {
    @NoAuth
    @Provides
    @Singleton
    fun provideNoAuthOkHttpClient(
        @UserAgent userAgentInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(userAgentInterceptor)
        }.build()
    }

    @NoAuth
    @Provides
    @Singleton
    fun provideNoAuthRetrofit(
        @ServerUrl baseUrl: String,
        @NoAuth okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Auth retrofit instances below
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
    ): BusApi {
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

    @Provides
    @Singleton
    fun provideArticleApi(
        @NoAuth retrofit: Retrofit
    ): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoopShopApi(
        @NoAuth retrofit: Retrofit
    ): CoopShopApi {
        return retrofit.create(CoopShopApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimetableApi(
        @NoAuth retrofit: Retrofit
    ): TimetableApi {
        return retrofit.create(TimetableApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(
        @NoAuth retrofit: Retrofit
    ): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBannerApi(
        @NoAuth retrofit: Retrofit
    ): BannerApi {
        return retrofit.create(BannerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClubApi(
        @NoAuth retrofit: Retrofit
    ): ClubApi {
        return retrofit.create(ClubApi::class.java)
    }
}
