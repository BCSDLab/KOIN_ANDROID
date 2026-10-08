package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo

@Entity(
    tableName = DBConstant.COOP_SHOP,
    primaryKeys = ["id", "semester", "name"]
)
data class CoopShopEntity(
    val id: Int,
    val coopNameId: Int?,
    val name: String,
    val semester: String,
    val opens: List<OpenCloseInfo>,
    val phone: String,
    val location: String,
    val remarks: String,
    val updatedAt: String
)
