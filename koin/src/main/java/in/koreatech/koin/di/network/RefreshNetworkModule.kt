package `in`.koreatech.koin.di.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.Auth
import `in`.koreatech.koin.core.qualifier.Refresh
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.util.TokenAuthenticator
import `in`.koreatech.koin.util.ext.newRequest
import `in`.koreatech.koin.util.ext.putAccessToken
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
object RefreshNetworkModule {
    @Refresh
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenLocalDataSource: TokenLocalDataSource
    ): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val accessToken = tokenLocalDataSource.getAccessToken() ?: ""
                val newRequest: Request = chain.request().newRequest {
                    putAccessToken(accessToken)
                }
                chain.proceed(newRequest)
            }
        }
    }

    @Refresh
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Refresh authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(authInterceptor)
        }.build()
    }

    @Refresh
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @ServerUrl baseUrl: String,
        @Refresh okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /* Auth(for refresh) retrofit instances below */
    @Refresh
    @Provides
    @Singleton
    fun provideUserAuthApi(
        @Refresh retrofit: Retrofit
    ) : UserAuthApi {
        return retrofit.create(UserAuthApi::class.java)
    }
}