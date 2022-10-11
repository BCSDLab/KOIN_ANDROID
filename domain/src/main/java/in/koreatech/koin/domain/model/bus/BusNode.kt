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