package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable

@Immutable
data class CommonTimetableState(
    val amDepartures: List<DepartureState>,
    val pmDepartures: List<DepartureState>,
)

@JvmInline
value class DepartureState(
    val departureTime: String,
)
