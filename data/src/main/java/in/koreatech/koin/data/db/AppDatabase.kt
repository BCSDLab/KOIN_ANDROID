package `in`.koreatech.koin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.entity.CacheMetadataEntity
import `in`.koreatech.koin.data.entity.StoreCategoriesEntity

@Database(entities = [CacheMetadataEntity::class, StoreCategoriesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
    abstract fun storeCategoriesDao(): StoreCategoriesDao
}
