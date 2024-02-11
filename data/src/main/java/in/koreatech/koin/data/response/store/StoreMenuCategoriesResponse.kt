package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreMenuCategoriesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("menus") val menus: List<ShopMenusResponse>?,
)
