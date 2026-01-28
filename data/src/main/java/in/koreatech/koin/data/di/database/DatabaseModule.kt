package `in`.koreatech.koin.data.di.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.db.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "koin_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCacheMetadataDao(appDatabase: AppDatabase): CacheMetadataDao {
        return appDatabase.cacheMetadataDao()
    }

    @Provides
    @Singleton
    fun provideStoreCategoriesDao(appDatabase: AppDatabase): StoreCategoriesDao {
        return appDatabase.storeCategoriesDao()
    }
}