package `in`.koreatech.koin.data.response.land

import com.google.gson.annotations.SerializedName

data class LandResponse(
    @SerializedName("internal_name")
    val internalName: String?,
    @SerializedName("monthly_fee")
    val monthlyFee: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("charter_fee")
    val charterFee: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("room_type")
    val roomType: String?
)

data class LandsResponse(
    @SerializedName("lands")
    val lands: List<LandResponse>
)