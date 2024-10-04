package `in`.koreatech.koin.data.di.source

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import `in`.koreatech.koin.data.source.local.ArticleLocalDataSource
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.UserLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideSignupLocalDataSource(
        @ApplicationContext applicationContext: Context,
        @IoDispatcher dispatcherIO: CoroutineDispatcher
    ): SignupTermsLocalDataSource {
        return SignupTermsLocalDataSource(applicationContext, dispatcherIO)
    }

    @Provides
    @Singleton
    fun provideTokenLocalDataSource(
        @ApplicationContext applicationContext: Context,
        @IoDispatcher dispatcherIO: CoroutineDispatcher
    ): TokenLocalDataSource {
        return TokenLocalDataSource(applicationContext, dispatcherIO)
    }

    @Provides
    @Singleton
    fun provideVersionLocalDataSource(
        @ApplicationContext applicationContext: Context
    ): VersionLocalDataSource {
        return VersionLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideBusLocalDataSource(
        @ApplicationContext applicationContext: Context
    ): BusLocalDataSource {
        return BusLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideDeptLocalDataSource(
        @ApplicationContext applicationContext: Context
    ): DeptLocalDataSource {
        return DeptLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(
        @ApplicationContext applicationContext: Context,
    ): UserLocalDataSource {
        return UserLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideArticleLocalDataSource(
        articleDataStore: ArticleDataStore
    ): ArticleLocalDataSource {
        return ArticleLocalDataSource(articleDataStore)
    }
}