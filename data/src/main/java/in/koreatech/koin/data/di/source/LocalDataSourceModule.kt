package `in`.koreatech.koin.data.di.source

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.dao.ABTestDao
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import `in`.koreatech.koin.data.source.datastore.SessionDataStore
import `in`.koreatech.koin.data.source.datastore.SettingDataStore
import `in`.koreatech.koin.data.source.local.ArticleLocalDataSource
import `in`.koreatech.koin.data.source.local.CacheLocalDataSource
import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.local.SessionLocalDataSource
import `in`.koreatech.koin.data.source.local.SettingLocalDataSource
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.StoreLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.UserLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

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
    fun provideDeptLocalDataSource(
        @ApplicationContext applicationContext: Context
    ): DeptLocalDataSource {
        return DeptLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(
        @ApplicationContext applicationContext: Context,
        abTestDao: ABTestDao
    ): UserLocalDataSource {
        return UserLocalDataSource(applicationContext, abTestDao)
    }

    @Provides
    @Singleton
    fun provideArticleLocalDataSource(articleDataStore: ArticleDataStore): ArticleLocalDataSource {
        return ArticleLocalDataSource(articleDataStore)
    }

    @Provides
    @Singleton
    fun provideStoreLocalDataSource(storeCategoriesDao: StoreCategoriesDao): StoreLocalDataSource {
        return StoreLocalDataSource(storeCategoriesDao)
    }

    @Provides
    @Singleton
    fun provideSessionLocalDataSource(sessionDataStore: SessionDataStore): SessionLocalDataSource {
        return SessionLocalDataSource(sessionDataStore)
    }

    @Provides
    @Singleton
    fun provideSettingLocalDataSource(settingDataStore: SettingDataStore): SettingLocalDataSource {
        return SettingLocalDataSource(settingDataStore)
    }

    @Provides
    @Singleton
    fun provideCacheLocalDataSource(cacheMetadataDao: CacheMetadataDao): CacheLocalDataSource {
        return CacheLocalDataSource(cacheMetadataDao)
    }
}
