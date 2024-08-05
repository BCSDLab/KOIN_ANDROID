package `in`.koreatech.koin.domain.model.coopshop

enum class CoopShopDayType(
    value: String
) {
    Weekday("평일"),
    Weekend("주말")
}

val String.toCoopShopDayType
    get() = when (this) {
        "평일" -> CoopShopDayType.Weekday
        "주말" -> CoopShopDayType.Weekend
        else -> throw IllegalArgumentException("Invalid day type")
    }