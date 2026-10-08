package `in`.koreatech.koin.domain.model.coopshop

enum class CoopShopDayType(
    val value: String
) {
    Weekday("평일"),
    Weekend("주말"),
    Monday("월요일"),
    Tuesday("화요일"),
    Wednesday("수요일"),
    Thursday("목요일"),
    Friday("금요일"),
    Saturday("토요일"),
    Sunday("일요일")
}

val String.toCoopShopDayType
    get() = when (this) {
        "평일" -> CoopShopDayType.Weekday
        "주말" -> CoopShopDayType.Weekend
        "월요일" -> CoopShopDayType.Monday
        "화요일" -> CoopShopDayType.Tuesday
        "수요일" -> CoopShopDayType.Wednesday
        "목요일" -> CoopShopDayType.Thursday
        "금요일" -> CoopShopDayType.Friday
        "토요일" -> CoopShopDayType.Saturday
        "일요일" -> CoopShopDayType.Sunday
        else -> throw IllegalArgumentException("Invalid day type")
    }
