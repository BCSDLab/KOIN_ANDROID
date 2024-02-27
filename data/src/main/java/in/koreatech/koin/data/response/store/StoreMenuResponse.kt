package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreMenuResponse(
    @SerializedName("menu_categories") val menuCategories: List<StoreMenuCategoriesResponse>?,
)