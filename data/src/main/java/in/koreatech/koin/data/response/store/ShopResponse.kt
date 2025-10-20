package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopResponse(
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_delivery_available") val isDeliveryAvailable: Boolean,
    @SerializedName("is_takeout_available") val isTakeoutAvailable: Boolean,
    @SerializedName("service_event") val serviceEvent: Boolean,
    @SerializedName("minimum_order_amount") val minimumOrderAmount: Int,
    @SerializedName("rating_average") val ratingAverage: Double,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("minimum_delivery_tip") val minimumDeliveryTip: Int,
    @SerializedName("maximum_delivery_tip") val maximumDeliveryTip: Int,
    @SerializedName("is_open") val isOpen: Boolean,
    @SerializedName("category_ids") val categoryIds: List<Int>,
    @SerializedName("images") val images: List<ShopImagesResponse>,
    @SerializedName("open_status") val openStatus: String
) {
    data class ShopImagesResponse(
        @SerializedName("image_url") val imageUrl: String,
        @SerializedName("is_thumbnail") val isThumbnail: Boolean
    )

    data class OrderStoreShopsOpenResponse(
        @SerializedName("day_of_week") val dayOfWeek: String,
        @SerializedName("closed") val closed: Boolean,
        @SerializedName("open_time") val openTime: String,
        @SerializedName("close_time") val closeTime: String
    )
}
