package `in`.koreatech.koin.domain.model.bus.city

enum class CityBusNumber(
    val number: Int
) {
    Bus400(400),
    Bus402(402),
    Bus405(405)
}

val CityBusNumber.spinnerSelection
    get() = when (this) {
        CityBusNumber.Bus400 -> 0
        CityBusNumber.Bus402 -> 1
        CityBusNumber.Bus405 -> 2
    }

val Int.posToCityBusNumber
    get() = when (this) {
        0 -> CityBusNumber.Bus400
        1 -> CityBusNumber.Bus402
        2 -> CityBusNumber.Bus405
        else -> throw IllegalArgumentException("Not supported selection.")
    }

val Int.numberToCityBusNumber
    get() = when (this) {
        400 -> CityBusNumber.Bus400
        402 -> CityBusNumber.Bus402
        405 -> CityBusNumber.Bus405
        else -> throw IllegalArgumentException("Not supported selection.")
    }
