package `in`.koreatech.koin.domain.model.coopshop

data class OpenCloseInfo(
    val dayOfWeek: CoopShopDayType,
    val opensByDayType: List<OpenCloseTimeInfo>
)

data class OpenCloseTimeInfo(
    val type: String,
    val openTime: String,
    val closeTime: String
)

val String.checkIfNotOpen get() =
    if (this == "휴점(예약)") "미운영" else this