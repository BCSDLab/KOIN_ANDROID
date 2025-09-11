package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("orders") val orders: List<OrderHistoryOrders>
)

data class OrderHistoryOrders(
    @SerializedName("id") val id: Int,
    @SerializedName("payment_id") val paymentId: Int,
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("orderable_shop_name") val orderableShopName: String,
    @SerializedName("open_status") val openStatus: String,
    @SerializedName("orderable_shop_thumbnail") val orderableShopThumbnail: String,
    @SerializedName("order_date") val orderDate: String,
    @SerializedName("order_status") val orderStatus: String,
    @SerializedName("order_title") val orderTitle: String,
    @SerializedName("total_amount") val totalAmount: Int
)
