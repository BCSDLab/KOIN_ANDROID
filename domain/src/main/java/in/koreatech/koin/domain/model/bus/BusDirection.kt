package `in`.koreatech.koin.domain.model.bus

sealed class BusDirection(
    val busDirectionString: String
) {
    object ToKoreatech: BusDirection("to") // 학교로
    object FromKoreatech: BusDirection("from") // 학교에서
}

fun String.toBusDirection() = when(this) {
    "to" -> BusDirection.ToKoreatech
    "from" -> BusDirection.FromKoreatech
    else -> throw IllegalArgumentException("Not a bus node string.")
}