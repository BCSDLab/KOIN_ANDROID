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