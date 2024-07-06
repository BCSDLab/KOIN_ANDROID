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
import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.UploadUrlApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
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
                val ownerAccessToken =
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5ODg2Nzc4fQ.5LjbuPxi1G0aQoxtvWjB3ajpd28k6kjjCp4I199fkt3C8cRa40zxcjJwS91X0pSi"
               //     "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5ODAwNTA3fQ.5JP2LEvsvGGLt_0XdiyS-XzP_9y79CXfs7MVqdin8YIwFF1v3W43RYRqsy5ILUAR"
               //     "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzQ0NjMwfQ.lFGkh9EYK4dNfNcm765XiJ40BgWUFEJEZ__G4917xj74BUvqlp-CqxFxBBvyLs8D"
                //    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzM4MzAzfQ.YD-w5GLOziL8vxIH9snuzNPJmkweaEUGrgUoT3_Zf7VYkYiQ8IhNuAwbIwXi-59s"
                 //   "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzMwNTY4fQ.fyZZEt8ZX4eTSUQ0FA8uxKvHAQLoXjKMwad8FcE-E-mwhVbpX9fAqh2Arxw0GtMx"
                  //  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzI4Mjc5fQ.kfvy-qGmYvOD-FtAWEPZ4zNsKOoz-l-xGW7R4Wb42eWhuhgcPHcOLs_RixPJD6Rb"
                    //"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzIwODUzfQ.GhRRGmlAT16kT3QRjS9dT4-ZKIih1od30-0Ca9Ee2DS6iVhFVHXZnwp5vSjJiMCs"
                   // "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NzIwNjE5fQ.ZMyWalzI9gTpLBUnIWj6XNotKsTuZRUSOC-cDainytso4QdBRSAjwvj9NA2x2dcy"
                 //   "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NjM3NjU3fQ.w_FsTfvI59yErcUEyBhseivHgohlfaV_SJRmJ9Z2YTT56EJfpKQR2HWap5I7_E_G"
                  //  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NjMxMzM3fQ.pBmetpcS99QZAwgR93DPY0s1kntW518WuoodRVNrAR1RNzPxOw7-b22Qe9-n1vrV"
                    //"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpZCI6NTU0OCwiZXhwIjoxNzE5NTU1MTI1fQ.eYUnpAuN9cEAMbgveHOoIs2mZ_i4F3taALMRvMm4X9XnQgyYQQk1veHQhEYLSnvF"//tokenLocalDataSource.getOwnerAccessToken() ?: ""
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