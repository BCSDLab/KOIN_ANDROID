package `in`.koreatech.koin.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.dao.ABTestDao
import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.dao.CoopShopDao
import `in`.koreatech.koin.data.dao.DiningDao
import `in`.koreatech.koin.data.dao.NotificationDao
import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.dao.WeatherDao
import `in`.koreatech.koin.data.entity.ABTestEntity
import `in`.koreatech.koin.data.entity.CacheMetadataEntity
import `in`.koreatech.koin.data.entity.CoopShopEntity
import `in`.koreatech.koin.data.entity.DiningEntity
import `in`.koreatech.koin.data.entity.NotificationEntity
import `in`.koreatech.koin.data.entity.StoreCategoriesEntity
import `in`.koreatech.koin.data.entity.WeatherEntity
import `in`.koreatech.koin.data.util.DateTimeConverter
import `in`.koreatech.koin.data.util.OpenCloseInfoListConverter
import `in`.koreatech.koin.data.util.StringListConverter

@Database(
    entities = [
        CacheMetadataEntity::class,
        StoreCategoriesEntity::class,
        ABTestEntity::class,
        NotificationEntity::class,
        WeatherEntity::class,
        DiningEntity::class,
        CoopShopEntity::class
    ],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
    ]
)
@TypeConverters(DateTimeConverter::class, StringListConverter::class, OpenCloseInfoListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
    abstract fun storeCategoriesDao(): StoreCategoriesDao
    abstract fun abTestDao(): ABTestDao
    abstract fun notificationDao(): NotificationDao
    abstract fun weatherDao(): WeatherDao
    abstract fun diningDao(): DiningDao
    abstract fun coopShopDao(): CoopShopDao

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
