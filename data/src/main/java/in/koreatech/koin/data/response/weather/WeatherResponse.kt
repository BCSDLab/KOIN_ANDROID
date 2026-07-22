package `in`.koreatech.koin.data.response.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("weather") val weather: String,
    @SerializedName("weather_id") val weatherId: Int,
    @SerializedName("weather_icon_url") val weatherIconUrl: String
)
