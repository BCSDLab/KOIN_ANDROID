package `in`.koreatech.koin.di.db

import `in`.koreatech.koin.data.database.AppDatabase
import `in`.koreatech.koin.data.database.MenuItemDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "MenuItem_db")
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideMenuItemDao(appDatabase: AppDatabase): MenuItemDao {
        return appDatabase.menuItemDao()
    }
}