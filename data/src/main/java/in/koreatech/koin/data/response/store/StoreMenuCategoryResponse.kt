package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreMenuCategoryResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("menu_categories") val menuCategories: List<MenuCategory>
){
    data class MenuCategory(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )
}