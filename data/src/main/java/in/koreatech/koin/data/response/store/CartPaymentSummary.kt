package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class CartPaymentSummary(
    @SerializedName("item_total_amount") val itemTotalAmount: Int,
    @SerializedName("delivery_fee") val deliveryFee: Int,
    @SerializedName("total_amount") val totalAmount: Int,
    @SerializedName("final_payment_amount") val finalPaymentAmount: Int
)
