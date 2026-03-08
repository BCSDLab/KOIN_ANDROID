package `in`.koreatech.koin.feature.callvan.ui.list.model

data class CallvanListUiState(
    val departure: String,
    val destination: String,
    val date: String,
    val dayOfWeek: String,
    val time: String,
    val currentCount: Int,
    val maxCount: Int,
)