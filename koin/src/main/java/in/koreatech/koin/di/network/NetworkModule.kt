package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.core.qualifier.ServerUrl
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.util.ext.isDebug
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(
        @ApplicationContext applicationContext: Context
    ) = HttpLoggingInterceptor().apply {
        level = if (applicationContext.isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @ServerUrl
    @Provides
    @Singleton
    fun provideServerUrl(
        @ApplicationContext applicationContext: Context
    ): String {
        return if (applicationContext.isDebug) {
            URLConstant.BASE_URL_STAGE
        } else {
            URLConstant.BASE_URL_PRODUCTION
        }
    }
}