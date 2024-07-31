package `in`.koreatech.koin.domain.model.bus.city

enum class CityBusGeneralDestination(
    val destination: String
) {
    Terminal("터미널"),
    Beongchon("병천")
}

val CityBusGeneralDestination.toggleSelection
    get() = when (this) {
        CityBusGeneralDestination.Beongchon -> 0
        CityBusGeneralDestination.Terminal -> 1
    }

val Int.toCityBusGeneralDestination
    get() = when (this) {
        0 -> CityBusGeneralDestination.Beongchon
        1 -> CityBusGeneralDestination.Terminal
        else -> throw IllegalArgumentException("Not supported selection.")
    }

val String.toCityBusGeneralDestination
    get() = when (this) {
        CityBusSpecificDestination.Terminal.destination -> CityBusGeneralDestination.Terminal
        CityBusSpecificDestination.Beongchon400.destination,
        CityBusSpecificDestination.Beongchon402.destination,
        CityBusSpecificDestination.Beongchon405.destination -> CityBusGeneralDestination.Beongchon
        else -> throw IllegalArgumentException("Not supported node string")
    }

val CityBusSpecificDestination.toCityBusGeneralDestination
    get() = when (this) {
        CityBusSpecificDestination.Terminal -> CityBusGeneralDestination.Terminal
        CityBusSpecificDestination.Beongchon400,
        CityBusSpecificDestination.Beongchon402,
        CityBusSpecificDestination.Beongchon405 -> CityBusGeneralDestination.Beongchon
    }
