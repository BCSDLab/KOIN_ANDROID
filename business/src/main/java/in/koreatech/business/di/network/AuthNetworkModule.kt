package `in`.koreatech.business.di.network

import `in`.koreatech.koin.core.qualifier.Auth
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.business.util.RefreshTokenInterceptor
import javax.inject.Singleton
import `in`.koreatech.koin.core.qualifier.REFRESH
import `in`.koreatech.koin.data.api.UserApi
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

    @REFRESH
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
        @REFRESH refreshInterceptor: Interceptor
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
    ) : UserAuthApi {
        return retrofit.create(UserAuthApi::class.java)
    }
}