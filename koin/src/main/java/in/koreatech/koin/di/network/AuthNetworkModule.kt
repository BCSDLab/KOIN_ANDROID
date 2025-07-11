package `in`.koreatech.koin.di.network

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.Auth
import `in`.koreatech.koin.core.qualifier.Inspection
import `in`.koreatech.koin.core.qualifier.OwnerAuth
import `in`.koreatech.koin.core.qualifier.OwnerUserAgent
import `in`.koreatech.koin.core.qualifier.PreSignedUrl
import `in`.koreatech.koin.core.qualifier.PreSignedUserAgent
import `in`.koreatech.koin.core.qualifier.Refresh
import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.core.qualifier.UserAgent
import `in`.koreatech.koin.data.api.PreSignedUrlApi
import `in`.koreatech.koin.data.api.UploadUrlApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.ArticleAuthApi
import `in`.koreatech.koin.data.api.auth.ChatAuthApi
import `in`.koreatech.koin.data.api.auth.ClubAuthApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.api.auth.StoreAuthApi
import `in`.koreatech.koin.data.api.auth.TimetableAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.util.EmptyStringToNullAdapter
import `in`.koreatech.koin.di.userAgent.UserAgentInterceptor
import `in`.koreatech.koin.di.userAgent.UserAgentProvider
import `in`.koreatech.koin.domain.usecase.user.DeleteUserRefreshTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateUserRefreshTokenUseCase
import `in`.koreatech.koin.util.OwnerTokenAuthenticator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    @UserAgent
    @Provides
    @Singleton
    fun provideUserAgentInterceptor(
        userAgentProvider: UserAgentProvider
    ): Interceptor = UserAgentInterceptor(userAgentProvider)

    @Provides
    @Singleton
    @Inspection
    fun provideInspectionInterceptor(
        @ApplicationContext context: Context
    ): Interceptor = InspectionInterceptor(context)

    @Auth
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenLocalDataSource: TokenLocalDataSource): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val accessToken = tokenLocalDataSource.getAccessToken() ?: ""
                val historyId = tokenLocalDataSource.getAccessHistoryId() ?: ""
                val newRequest: Request =
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .addHeader("access_history_id", historyId)
                        .build()
                chain.proceed(newRequest)
            }
        }
    }

    @Refresh
    @Provides
    @Singleton
    fun provideRefreshInterceptor(
        @ApplicationContext context: Context,
        tokenLocalDataSource: TokenLocalDataSource,
        updateUserRefreshTokenUseCase: UpdateUserRefreshTokenUseCase,
        deleteUserRefreshTokenUseCase: DeleteUserRefreshTokenUseCase,
        userApi: UserApi
    ): Authenticator = AuthAuthenticator(
        context,
        tokenLocalDataSource,
        updateUserRefreshTokenUseCase,
        deleteUserRefreshTokenUseCase,
        userApi
    )

    @Auth
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Inspection inspectionInterceptor: Interceptor,
        @UserAgent userAgentInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
        @Refresh refreshInterceptor: Authenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(authInterceptor)
            addInterceptor(inspectionInterceptor)
            authenticator(refreshInterceptor)
            addInterceptor(userAgentInterceptor)
        }.build()
    }

    @Auth
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @ServerUrl baseUrl: String,
        @Auth okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder().registerTypeAdapter(String::class.java, EmptyStringToNullAdapter()).create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Auth retrofit instances below
    @Provides
    @Singleton
    fun provideUserAuthApi(@Auth retrofit: Retrofit): UserAuthApi {
        return retrofit.create(UserAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUploadUrlApi(@Auth retrofit: Retrofit): UploadUrlApi {
        return retrofit.create(UploadUrlApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleAuthApi(@Auth retrofit: Retrofit): ArticleAuthApi {
        return retrofit.create(ArticleAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimetableAuthApi(@Auth retrofit: Retrofit): TimetableAuthApi {
        return retrofit.create(TimetableAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatAuthApi(@Auth retrofit: Retrofit): ChatAuthApi {
        return retrofit.create(ChatAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClubAuthApi(@Auth retrofit: Retrofit): ClubAuthApi {
        return retrofit.create(ClubAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreAuthApi(@Auth retrofit: Retrofit): StoreAuthApi {
        return retrofit.create(StoreAuthApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object OwnerAuthNetworkModule {
    @OwnerUserAgent
    @Provides
    @Singleton
    fun provideOwnerUserAgentInterceptor(
        userAgentProvider: UserAgentProvider
    ): Interceptor = UserAgentInterceptor(userAgentProvider)

    @OwnerAuth
    @Provides
    @Singleton
    fun provideOwnerAuthInterceptor(tokenLocalDataSource: TokenLocalDataSource): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val ownerAccessToken = tokenLocalDataSource.getOwnerAccessToken() ?: ""
                val newRequest: Request =
                    chain.request().newBuilder()
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
        @OwnerUserAgent userAgentInterceptor: Interceptor,
        @OwnerAuth ownerAuthInterceptor: Interceptor,
        @OwnerAuth tokenAuthenticator: OwnerTokenAuthenticator,
        @Inspection inspectionInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(inspectionInterceptor)
            addInterceptor(ownerAuthInterceptor)
            authenticator(tokenAuthenticator)
            addInterceptor(userAgentInterceptor)
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
    fun provideOwnerAuthApi(@OwnerAuth retrofit: Retrofit): OwnerAuthApi {
        return retrofit.create(OwnerAuthApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PreSignedUrlNetworkModule {
    @PreSignedUserAgent
    @Provides
    @Singleton
    fun providePreSignedUserAgentInterceptor(
        userAgentProvider: UserAgentProvider
    ): Interceptor = UserAgentInterceptor(userAgentProvider)

    @PreSignedUrl
    @Provides
    @Singleton
    fun provideOwnerAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @PreSignedUserAgent userAgentInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(userAgentInterceptor)
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
    fun provideUploadUrlApi(@PreSignedUrl retrofit: Retrofit): PreSignedUrlApi {
        return retrofit.create(PreSignedUrlApi::class.java)
    }
}
