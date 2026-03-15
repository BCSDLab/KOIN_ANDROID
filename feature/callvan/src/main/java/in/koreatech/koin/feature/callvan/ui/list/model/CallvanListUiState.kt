package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.compose.runtime.Immutable

@Immutable
data class CallvanListUiState(
    val departure: String,
    val destination: String,
    val date: String,
    val time: String,
    val currentCount: Int,
    val maxCount: Int,
    val itemState: CallvanItemState
)