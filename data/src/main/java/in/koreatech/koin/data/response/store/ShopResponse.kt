package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopResponse(
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_delivery_available") val isDeliveryAvailable: Boolean,
    @SerializedName("is_takeout_available") val isTakeoutAvailable: Boolean,
    @SerializedName("minimum_order_amount") val minimumOrderAmount: Int,
    @SerializedName("rating_average") val ratingAverage: Int,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("minimum_delivery_tip") val minimumDeliveryTip: Int,
    @SerializedName("maximum_delivery_tip") val maximumDeliveryTip: Int,
    @SerializedName("is_open") val isOpen: Boolean,
    @SerializedName("category_ids") val categoryIds: List<Int>,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("open") val open: List<OrderStoreShopsOpenResponse>,
    @SerializedName("open_status") val openStatus: String
) {
    data class OrderStoreShopsOpenResponse(
        @SerializedName("day_of_week") val dayOfWeek: Int,
        @SerializedName("closed") val closed: Boolean,
        @SerializedName("open_time") val openTime: String,
        @SerializedName("close_time") val closeTime: String
    )
}
