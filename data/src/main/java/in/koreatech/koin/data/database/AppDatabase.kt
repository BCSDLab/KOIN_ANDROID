package `in`.koreatech.koin.data.database

import `in`.koreatech.koin.data.entity.MenuItemEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MenuItemEntity::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
}