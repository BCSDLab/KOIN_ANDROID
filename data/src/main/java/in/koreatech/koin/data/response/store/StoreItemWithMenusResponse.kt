package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreItemWithMenusResponse(
    @SerializedName("id") val uid: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("delivery") val isDeliveryOk: Boolean?,
    @SerializedName("delivery_price") val deliveryPrice: Int?,
    @SerializedName("pay_card") val isCardOk: Boolean?,
    @SerializedName("pay_bank") val isBankOk: Boolean?,
    @SerializedName("open") val open: List<StoreItemResponse.OpenResponseDTO>?,
    @SerializedName("image_urls") val imageUrls: ArrayList<String>?,
    @SerializedName("updated_at") val updateAt: String,
    @SerializedName("is_event") val isEvent: Boolean?,
    @SerializedName("shop_categories") val shopCategories: List<CategoriesResponseDTO>?,
    @SerializedName("menu_categories") val menuCategories: List<CategoriesResponseDTO>?,
) {
    data class CategoriesResponseDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
    )
}