package `in`.koreatech.koin.data.response.bus.express

import com.google.gson.annotations.SerializedName

data class ExpressDepartureArrivalResponse(
    @SerializedName("departure") val departure: String,
    @SerializedName("arrival") val arrival: String,
)
