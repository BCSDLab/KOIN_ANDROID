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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.articleDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "article_data_store"
    )

    @Provides
    @Singleton
    fun provideArticleDataStore(
        @ApplicationContext context: Context
    ): ArticleDataStore {
        return ArticleDataStore(context.articleDataStore)
    }
}