package `in`.koreatech.bus.navigation

import kotlinx.serialization.Serializable

internal object Routes {

    @Serializable data object BusTimetable

    @Serializable data object BusSearch
    @Serializable data class BusSearchResult(val departure: String, val arrival: String)
}