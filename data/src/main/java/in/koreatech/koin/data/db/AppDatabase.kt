package `in`.koreatech.koin.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.dao.ABTestDao
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.entity.ABTestEntity
import `in`.koreatech.koin.data.entity.CacheMetadataEntity
import `in`.koreatech.koin.data.entity.StoreCategoriesEntity

@Database(
    entities = [CacheMetadataEntity::class, StoreCategoriesEntity::class, ABTestEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
    abstract fun storeCategoriesDao(): StoreCategoriesDao
    abstract fun abTestDao(): ABTestDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `${DBConstant.STORE_CATEGORIES}`")
                db.execSQL("CREATE TABLE IF NOT EXISTS `${DBConstant.STORE_CATEGORIES}` (`id` INTEGER NOT NULL, `order` INTEGER NOT NULL, `image_url` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))")
                db.execSQL("DELETE FROM `${DBConstant.CACHE_METADATA}` WHERE cacheKey = '${DBConstant.STORE_CATEGORIES}'")
            }
        }
    }
}
