package `in`.koreatech.koin.data.di.network

import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.constant.URLConstant
import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.BuildConfig
//import `in`.koreatech.koin.domain.util.ext.isDebug
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(

    ) = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @ServerUrl
    @Provides
    @Singleton
    fun provideServerUrl(

    ): String {
        return if (BuildConfig.DEBUG) {
            URLConstant.BASE_URL_STAGE
        } else {
            URLConstant.BASE_URL_PRODUCTION
        }
    }
}
