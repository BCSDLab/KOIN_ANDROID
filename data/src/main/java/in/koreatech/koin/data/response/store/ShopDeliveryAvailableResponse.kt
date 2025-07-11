package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopDeliveryAvailableResponse(
    @SerializedName("campus_delivery") val campusDelivery: Boolean,
    @SerializedName("off_campus_delivery") val offCampusDelivery: Boolean
)
