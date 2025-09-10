package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class OrderInProgressResponse(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("order_type") val orderType: String,
    @SerializedName("shop_name") val shopName: String,
    @SerializedName("shop_thumbnail") val shopThumbnail: String,
    @SerializedName("estimated_at") val estimatedAt: String?,
    @SerializedName("order_status") val orderStatus: String,
    @SerializedName("payment_description") val paymentDescription: String,
    @SerializedName("total_amount") val totalAmount: Int
)
