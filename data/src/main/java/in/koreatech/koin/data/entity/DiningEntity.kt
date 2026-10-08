package `in`.koreatech.koin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(
    tableName = DBConstant.DINING,
    primaryKeys = ["id", "date", "type", "place"]
)
data class DiningEntity(
    val id: Int,
    @ColumnInfo(defaultValue = "''")
    val cacheDate: String,
    val date: String,
    val type: String,
    val place: String,
    val priceCard: String,
    val priceCash: String,
    val kcal: String,
    val menu: List<String>,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val soldOutAt: String,
    val changedAt: String
)
