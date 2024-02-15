package `in`.koreatech.koin.data.database

import `in`.koreatech.koin.data.entity.MenuItemEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    suspend fun loadAllMenuItems(): List<MenuItemEntity>

    @Query("DELETE FROM menu_items")
    suspend fun deleteAllMenuItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMenuItems(menuItems: List<MenuItemEntity>)
}