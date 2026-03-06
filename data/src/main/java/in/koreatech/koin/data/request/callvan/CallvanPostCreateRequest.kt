package `in`.koreatech.koin.data.request.callvan

import com.google.gson.annotations.SerializedName

data class CallvanPostCreateRequest(
    @SerializedName("departure_type")
    val departureType: String,
    @SerializedName("departure_custom_name")
    val departureCustomName: String?,
    @SerializedName("arrival_type")
    val arrivalType: String,
    @SerializedName("arrival_custom_name")
    val arrivalCustomName: String?,
    @SerializedName("departure_date")
    val departureDate: String,
    @SerializedName("departure_time")
    val departureTime: String,
    @SerializedName("max_participants")
    val maxParticipants: Int
)
