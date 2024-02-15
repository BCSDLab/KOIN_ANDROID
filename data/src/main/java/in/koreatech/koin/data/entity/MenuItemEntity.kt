package `in`.koreatech.koin.data.entity

import androidx.room.Entity

@Entity(tableName = "menu_items",primaryKeys = [("title")])
data class MenuItemEntity(
    val title: String,
    val imageResource: Int,
    var isSelected: Boolean = false
)
