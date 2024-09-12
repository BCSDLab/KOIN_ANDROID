package `in`.koreatech.koin.domain.model.bus.city

enum class CityBusSpecificDestination(
    val destination: String
) {
    Terminal("종합터미널"),
    Beongchon400("병천3리"),
    Beongchon402("황사동"),
    Beongchon405("유관순열사사적지")
}

fun CityBusGeneralDestination.toCityBusSpecificDestination(number: CityBusNumber): CityBusSpecificDestination {
    when (this) {
        CityBusGeneralDestination.Terminal -> return CityBusSpecificDestination.Terminal
        CityBusGeneralDestination.Beongchon -> {
            when (number) {
                CityBusNumber.Bus400 -> return CityBusSpecificDestination.Beongchon400
                CityBusNumber.Bus402 -> return CityBusSpecificDestination.Beongchon402
                CityBusNumber.Bus405 -> return CityBusSpecificDestination.Beongchon405
            }
        }
    }
}