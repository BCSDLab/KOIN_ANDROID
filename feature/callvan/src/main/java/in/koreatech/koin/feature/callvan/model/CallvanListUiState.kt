package `in`.koreatech.koin.feature.callvan.model

data class CallvanListUiState(
    val departure: String,
    val destination: String,
    val date: String,
    val time: String,
    val currentCount: Int,
    val maxCount: Int
)
