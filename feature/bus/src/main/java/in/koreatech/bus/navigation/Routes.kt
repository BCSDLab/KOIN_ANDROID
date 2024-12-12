package `in`.koreatech.bus.navigation

import `in`.koreatech.bus.type.PlaceType
import kotlinx.serialization.Serializable

internal object Routes {

    @Serializable data object BusTimetable
    @Serializable data class ShuttleTimetableDetail(val id: String)

    @Serializable data object BusSearch
    @Serializable data class BusSearchResult(val departure: PlaceType, val arrival: PlaceType)
}