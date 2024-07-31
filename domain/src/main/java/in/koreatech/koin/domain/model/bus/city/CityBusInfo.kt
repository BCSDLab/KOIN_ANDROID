package `in`.koreatech.koin.domain.model.bus.city

data class CityBusInfo(
    val busNumber: Int,
    val direction: String
) {
    fun Pair<CityBusNumber, CityBusGeneralDestination>.toCityBusInfo(): CityBusInfo {
        val (busNumber, destination) = this
        val direction = destination.toCityBusSpecificDestination(busNumber).destination
        return CityBusInfo(busNumber.number, direction)
    }
}
