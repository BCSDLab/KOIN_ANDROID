package `in`.koreatech.bus.navigation

import kotlinx.serialization.Serializable

internal object Routes {

    @Serializable data object BusTimetable
    @Serializable data class ShuttleTimetableDetail(val route: String, val id: String)

    @Serializable data object BusSearch
    @Serializable data class BusSearchResult(val departure: String, val arrival: String)
}