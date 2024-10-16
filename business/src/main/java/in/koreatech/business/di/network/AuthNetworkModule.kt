package `in`.koreatech.business.di.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.business.util.OwnerTokenAuthenticator
import `in`.koreatech.business.util.RefreshTokenInterceptor
import `in`.koreatech.koin.core.qualifier.Auth
import `in`.koreatech.koin.core.qualifier.OwnerAuth
import `in`.koreatech.koin.core.qualifier.PreSignedUrl
import `in`.koreatech.koin.core.qualifier.Refresh
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.api.PreSignedUrlApi
import `in`.koreatech.koin.data.api.UploadUrlApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    @Auth
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenLocalDataSource: TokenLocalDataSource
    ): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val accessToken = tokenLocalDataSource.getAccessToken() ?: ""
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                chain.proceed(newRequest)
            }
        }
    }

    @Refresh
    @Provides
    @Singleton
    fun provideRefreshInterceptor(
        @ApplicationContext applicationContext: Context,
        tokenLocalDataSource: TokenLocalDataSource,
        userApi: UserApi
    ): Interceptor = RefreshTokenInterceptor(applicationContext, tokenLocalDataSource, userApi)


    @Auth
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Auth authInterceptor: Interceptor,
        @Refresh refreshInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(authInterceptor)
            addInterceptor(refreshInterceptor)
        }.build()
    }

    @Auth
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @ServerUrl baseUrl: String,
        @Auth okHttpClient: OkHttpClient
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
    fun provideUserAuthApi(
        @Auth retrofit: Retrofit
    ): UserAuthApi {
        return retrofit.create(UserAuthApi::class.java)
    }

}


@Module
@InstallIn(SingletonComponent::class)
object BusinessAuthNetworkModule {
    @OwnerAuth
    @Provides
    @Singleton
    fun provideOwnerAuthInterceptor(
        tokenLocalDataSource: TokenLocalDataSource
    ): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val ownerAccessToken = tokenLocalDataSource.getOwnerAccessToken() ?: ""
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $ownerAccessToken")
                    .build()
                chain.proceed(newRequest)
            }
        }
    }

    @OwnerAuth
    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        @ApplicationContext applicationContext: Context,
        tokenLocalDataSource: TokenLocalDataSource
    ) = OwnerTokenAuthenticator(applicationContext, tokenLocalDataSource)

    @OwnerAuth
    @Provides
    @Singleton
    fun provideOwnerAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @OwnerAuth ownerAuthInterceptor: Interceptor,
        @OwnerAuth tokenAuthenticator: OwnerTokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(ownerAuthInterceptor)
            authenticator(tokenAuthenticator)
        }.build()
    }

    @OwnerAuth
    @Provides
    @Singleton
    fun provideOwnerAuthRetrofit(
        @ServerUrl baseUrl: String,
        @OwnerAuth ownerOkHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(ownerOkHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUploadUrlApi(
        @OwnerAuth retrofit: Retrofit
    ): UploadUrlApi {
        return retrofit.create(UploadUrlApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOwnerAuthApi(
        @OwnerAuth retrofit: Retrofit
    ): OwnerAuthApi {
        return retrofit.create(OwnerAuthApi::class.java)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object PreSignedUrlNetworkModule {
    @PreSignedUrl
    @Provides
    @Singleton
    fun provideOwnerAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    @PreSignedUrl
    fun providePreSignedUrlRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://kap-test.s3.ap-northeast-2.amazonaws.com/")
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                    }
                ).build()
            ).build()
    }

    @Provides
    @Singleton
    fun provideUploadUrlApi(
        @PreSignedUrl retrofit: Retrofit
    ): PreSignedUrlApi {
        return retrofit.create(PreSignedUrlApi::class.java)
    }
}