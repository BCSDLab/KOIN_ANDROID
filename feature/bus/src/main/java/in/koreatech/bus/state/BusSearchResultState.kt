package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.koin.domain.model.bus.v2.BusSearchResult
import java.time.LocalTime

@Immutable
data class BusSearchResultState(
    val busType: BusType,
    val busName: String,
    val departureTime: LocalTime
)

fun BusSearchResult.toBusSearchResultState() = BusSearchResultState(
    busType = BusType.valueOf(busType.uppercase()),
    busName = busName,
    departureTime = departureTime
)
