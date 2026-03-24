package `in`.koreatech.koin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.STORE_CATEGORIES)
data class StoreCategoriesEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "name") val name: String
)
