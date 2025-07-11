package `in`.koreatech.koin.data.ordershop

import com.google.gson.annotations.SerializedName

data class OrderShopResponse(
    @SerializedName("shop_id")
    val shopId: Int,
    @SerializedName("orderable_shop_id")
    val orderableShopId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("is_delivery_available")
    val isDeliveryAvailable: Boolean,
    @SerializedName("is_takeout_available")
    val isTakeoutAvailable: Boolean,
    @SerializedName("pay_card")
    val payCard: Boolean,
    @SerializedName("pay_bank")
    val payBank: Boolean,
    @SerializedName("minimum_order_amount")
    val minimumOrderAmount: Int,
    @SerializedName("rating_average")
    val ratingAverage: Float,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("minimum_delivery_tip")
    val minimumDeliveryTip: Int,
    @SerializedName("maximum_delivery_tip")
    val maximumDeliveryTip: Int,
    @SerializedName("images")
    val images: List<image>,
){
    data class image(
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("is_thumbnail")
        val isThumbnail: Boolean,
    )
}