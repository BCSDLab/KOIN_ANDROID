package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanPostCreateResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: String,
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
    val maxParticipants: Int,
    @SerializedName("current_participants")
    val currentParticipants: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
