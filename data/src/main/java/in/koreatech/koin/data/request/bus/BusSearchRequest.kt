package `in`.koreatech.koin.data.request.bus

import com.google.gson.annotations.SerializedName

data class BusSearchRequest(
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("bus_type") val busType: String,
    @SerializedName("depart") val departure: String,
    @SerializedName("arrival") val arrival: String
)
