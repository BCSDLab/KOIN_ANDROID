package `in`.koreatech.koin.domain.model.bus

sealed class BusNode(
    val busNodeString: String
) {
    object Koreatech: BusNode("koreatech")
    object Station: BusNode("station")
    object Terminal: BusNode("terminal")
}

fun String.toBusNode() = when(this) {
    "koreatech" -> BusNode.Koreatech
    "station" -> BusNode.Station
    "terminal" -> BusNode.Terminal
    else -> throw IllegalArgumentException("Not a bus node string.")
}

val BusNode.spinnerSelection
    get() = when (this) {
        BusNode.Koreatech -> 0
        BusNode.Station -> 2
        BusNode.Terminal -> 1
    }

val Int.busNodeSelection
    get() = when (this) {
        0 -> BusNode.Koreatech
        2 -> BusNode.Station
        1 -> BusNode.Terminal
        else -> throw IllegalArgumentException("Not supported selection.")
    }