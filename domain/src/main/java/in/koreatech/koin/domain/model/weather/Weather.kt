package `in`.koreatech.koin.domain.model.weather

data class Weather(
    val temperature: Int,
    val weather: String,
    val weatherId: Int,
    val weatherIconUrl: String
)
