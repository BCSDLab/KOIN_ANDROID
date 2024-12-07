package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetableItem

@Immutable
data class ExpressTimetableItemState(
    val arrival: String,
    val departure: String,
    val charge: Int,
)

fun ExpressTimetableItem.toExpressTimetableItemState() = ExpressTimetableItemState(
    arrival = arrival,
    departure = departure,
    charge = charge,
)
