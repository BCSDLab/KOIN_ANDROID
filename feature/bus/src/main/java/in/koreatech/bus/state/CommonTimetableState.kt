package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable

@Immutable
data class CommonTimetableState(
    val amArrivals: List<ArrivalState>,
    val pmArrivals: List<ArrivalState>,
)

@JvmInline
value class ArrivalState(
    val arrivalTime: String,
)
