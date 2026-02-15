package `in`.koreatech.koin.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.koreatech.koin.data.dao.ABTestDao
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.entity.ABTestEntity
import `in`.koreatech.koin.data.entity.CacheMetadataEntity
import `in`.koreatech.koin.data.entity.StoreCategoriesEntity

@Database(
    entities = [CacheMetadataEntity::class, StoreCategoriesEntity::class, ABTestEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
    abstract fun storeCategoriesDao(): StoreCategoriesDao
    abstract fun abTestDao(): ABTestDao
}
