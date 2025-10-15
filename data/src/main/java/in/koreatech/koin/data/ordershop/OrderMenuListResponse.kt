package `in`.koreatech.koin.data.ordershop

import com.google.gson.annotations.SerializedName

data class OrderMenuListResponse(
    @SerializedName("menu_group_id") val menuGroupId: Int,
    @SerializedName("menu_group_name") val menuGroupName: String,
    @SerializedName("menus") val menus: List<OrderMenuResponse>
)
