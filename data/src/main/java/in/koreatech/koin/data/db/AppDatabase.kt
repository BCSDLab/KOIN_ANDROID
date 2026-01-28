package `in`.koreatech.koin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.entity.CacheMetadataEntity

@Database(entities = [CacheMetadataEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
}
