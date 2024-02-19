package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.core.qualifier.Auth
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.util.TokenAuthenticator
import `in`.koreatech.koin.util.ext.isDebug
import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.NoAuth
import `in`.koreatech.koin.core.qualifier.OwnerAuth
import `in`.koreatech.koin.data.api.business.MyStoreRegisterApi
import `in`.koreatech.koin.util.OwnerTokenAuthenticator
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object OwnerAuthNetworkModule {
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
    fun provideMyStoreRegisterApi(
        @OwnerAuth retrofit: Retrofit
    ): MyStoreRegisterApi {
        return retrofit.create(MyStoreRegisterApi::class.java)
    }

}