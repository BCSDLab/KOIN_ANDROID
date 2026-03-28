package `in`.koreatech.koin.domain.model.coopshop

enum class CoopShopDayType(
    val value: String
) {
    Weekday("평일"),
    Weekend("주말"),
    Saturday("토요일")
}

val String.toCoopShopDayType
    get() = when (this) {
        "평일" -> CoopShopDayType.Weekday
        "주말" -> CoopShopDayType.Weekend
        "토요일" -> CoopShopDayType.Saturday
        else -> throw IllegalArgumentException("Invalid day type")
    }
