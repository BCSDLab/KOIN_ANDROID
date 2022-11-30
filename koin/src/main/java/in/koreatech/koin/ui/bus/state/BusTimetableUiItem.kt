package `in`.koreatech.koin.ui.bus.state

sealed class BusTimetableUiItem {

    data class Express(
        val departureTime: String,
        val arrivalTime: String,
        val charge: String
    ) : BusTimetableUiItem()

    data class Shuttle(
        val node: String,
        val arrivalTime: String
    ): BusTimetableUiItem()

    data class City(
        val startLocation: String,
        val timeInfo: String
    ): BusTimetableUiItem()
}
