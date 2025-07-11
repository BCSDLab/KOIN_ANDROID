package `in`.koreatech.koin.data.ordershop
import com.google.gson.annotations.SerializedName

data class ShopOriginResponse(
    @SerializedName("shop_id")
    val shopId: Int,
    @SerializedName("orderable_shop_id")
    val orderableShopId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("open_time")
    val openTime: String?,
    @SerializedName("close_time")
    val closeTime: String?,
    @SerializedName("closed_days")
    val closedDays: List<String>,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("introduction")
    val introduction: String?,
    @SerializedName("notice")
    val notice: String?,
    @SerializedName("delivery_tips")
    val deliveryTips: List<DeliveryTip>,
    @SerializedName("owner_info")
    val ownerInfoResponse: OwnerInfoResponse,
    @SerializedName("origins")
    val origins: List<String>
){
    data class DeliveryTip(
        @SerializedName("order_price")
        val orderPrice: Int,

        @SerializedName("delivery_tip")
        val deliveryTip: Int
    )
    data class OwnerInfoResponse(
        @SerializedName("name")
        val name: String?,

        @SerializedName("shop_name")
        val shopName: String?,

        @SerializedName("address")
        val address: String?,

        @SerializedName("company_registration_number")
        val companyRegistrationNumber: String?
    )
}