package `in`.koreatech.koin.domain.model.bus

sealed class BusType(
    val busTypeString: String
) {
    object Shuttle : BusType("shuttle")
    object Commuting : BusType("commuting")
    object Express : BusType("express")
    object City : BusType("city")
}

fun String.toBusType() = when (this) {
    "shuttle" -> BusType.Shuttle
    "commuting" -> BusType.Commuting
    "express" -> BusType.Express
    "city" -> BusType.City
    else -> throw IllegalArgumentException("Not a bus type string")
}
