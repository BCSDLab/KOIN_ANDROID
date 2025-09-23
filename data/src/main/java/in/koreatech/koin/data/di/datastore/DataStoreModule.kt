package `in`.koreatech.koin.data.di.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import `in`.koreatech.koin.data.source.datastore.BannerDataStore
import `in`.koreatech.koin.data.source.datastore.BusDataStore
import `in`.koreatech.koin.data.source.datastore.SessionDataStore
import `in`.koreatech.koin.data.source.datastore.TimetableDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private val Context.articleDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "article_data_store"
    )

    private val Context.timetableDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "timetables"
    )

    private val Context.busDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "bus.ds"
    )

    private val Context.bannerDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "banner_data_Store"
    )

    private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "session_data_store"
    )

    @Provides
    @Singleton
    fun provideArticleDataStore(
        @ApplicationContext context: Context
    ): ArticleDataStore {
        return ArticleDataStore(context.articleDataStore)
    }

    @Provides
    @Singleton
    fun provideTimetableDataStore(
        @ApplicationContext context: Context
    ): TimetableDataStore {
        return TimetableDataStore(context.timetableDataStore)
    }

    @Provides
    @Singleton
    fun provideBusDataStore(
        @ApplicationContext context: Context
    ): BusDataStore {
        return BusDataStore(context.busDataStore)
    }

    @Provides
    @Singleton
    fun provideBannerDataStore(
        @ApplicationContext context: Context
    ): BannerDataStore {
        return BannerDataStore(context.bannerDataStore)
    }

    @Provides
    @Singleton
    fun provideSessionDataStore(
        @ApplicationContext context: Context
    ): SessionDataStore {
        return SessionDataStore(context.sessionDataStore)
    }
}
