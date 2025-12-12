package `in`.koreatech.koin.data.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.NoAuth
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.BuildConfig
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.stomp.KoinStomp
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.HEADERS
                }
        }

    @ServerUrl
    @Provides
    @Singleton
    fun provideServerUrl(): String {
        return if (BuildConfig.DEBUG) {
            URLConstant.BASE_URL_STAGE
        } else {
            URLConstant.BASE_URL_PRODUCTION
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpWebSocketClient(
        @NoAuth okHttpClient: OkHttpClient
    ): OkHttpWebSocketClient {
        return OkHttpWebSocketClient(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideStompClient(okHttpWebSocketClient: OkHttpWebSocketClient): StompClient {
        return StompClient(okHttpWebSocketClient) {
            heartBeat =
                HeartBeat(
                    minSendPeriod = 30000.milliseconds, // Follow backend recommendation
                    expectedPeriod = 30000.milliseconds
                )
        }
    }

    @Provides
    @Singleton
    fun provideKoinStomp(
        @ServerUrl baseUrl: String,
        tokenLocalDataSource: TokenLocalDataSource,
        stompClient: StompClient
    ): KoinStomp {
        return KoinStomp(baseUrl, tokenLocalDataSource, stompClient)
    }
}
