package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch

@Immutable
data class CallvanListUiState(
    val id: Int,
    val departure: String,
    val destination: String,
    val date: String,
    val time: String,
    val currentCount: Int,
    val maxCount: Int,
    val itemState: CallvanItemState
)

fun CallvanPostSearch.CallvanPost.toUiState(): CallvanListUiState = CallvanListUiState(
    id = id,
    departure = departure,
    destination = arrival,
    date = departureDate,
    time = departureTime,
    currentCount = currentParticipants,
    maxCount = maxParticipants,
    itemState = toItemState()
)
