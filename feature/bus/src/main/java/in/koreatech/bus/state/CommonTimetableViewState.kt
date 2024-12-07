package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.bus.type.DaytimeType

@Immutable
data class CommonTimetableViewState(
    val arrivals: Map<DaytimeType, List<ArrivalViewState>>,
    val updatedAt: String
)

data class ArrivalViewState(
    val arrivalTime: String,
)

// TODO : 임시 데이터, API 아직 없음