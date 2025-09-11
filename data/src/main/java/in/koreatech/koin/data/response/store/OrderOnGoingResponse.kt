package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class OrderOnGoingResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("payment_id") val paymentId: Int,
    @SerializedName("order_type") val orderType: String,
    @SerializedName("orderable_shop_name") val orderableShopName: String,
    @SerializedName("orderable_shop_thumbnail") val orderableShopThumbnail: String,
    @SerializedName("estimated_at") val estimatedAt: String,
    @SerializedName("order_status") val orderStatus: String,
    @SerializedName("order_title") val orderTitle: String,
    @SerializedName("total_amount") val totalAmount: Int
)
