package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopDetailResponse(
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("open_time") val openTime: String?,
    @SerializedName("close_time") val closeTime: String?,
    @SerializedName("closed_days") val closedDays: List<String>,
    @SerializedName("phone") val phone: String,
    @SerializedName("introduction") val introduction: String?,
    @SerializedName("notice") val notice: String?,
    @SerializedName("delivery_tips") val deliveryTips: List<ShopDetailDeliveryTipsResponse>,
    @SerializedName("owner_info") val ownerInfo: ShopDetailOwnerInfoResponse,
    @SerializedName("origins") val origins: List<ShopDetailOriginsResponse>
) {
    data class ShopDetailDeliveryTipsResponse(
        @SerializedName("from_amount") val fromAmount: Int,
        @SerializedName("to_amount") val toAmount: Int,
        @SerializedName("fee") val fee: Int
    )

    data class ShopDetailOwnerInfoResponse(
        @SerializedName("name") val name: String?,
        @SerializedName("shop_name") val shopName: String?,
        @SerializedName("address") val address: String?,
        @SerializedName("company_registration_number") val companyRegistrationNumber: String?
    )

    data class ShopDetailOriginsResponse(
        @SerializedName("ingredient") val ingredient: String,
        @SerializedName("origin") val origin: String
    )
}
