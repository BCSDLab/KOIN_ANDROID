package `in`.koreatech.koin.data.model.coopshop

import `in`.koreatech.koin.domain.model.coopshop.CoopShopDayType
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseTimeInfo
import `in`.koreatech.koin.domain.model.coopshop.toCoopShopDayType
import kotlinx.serialization.Serializable

@Serializable
data class CachedOpenCloseInfo(
    val dayOfWeek: String,
    val opensByDayType: List<CachedOpenCloseTimeInfo>
) {
    fun toOpenCloseInfo(): OpenCloseInfo = OpenCloseInfo(
        dayOfWeek = dayOfWeek.toCoopShopDayTypeOrLegacyName(),
        opensByDayType = opensByDayType.map(CachedOpenCloseTimeInfo::toOpenCloseTimeInfo)
    )

    companion object {
        fun from(info: OpenCloseInfo): CachedOpenCloseInfo = CachedOpenCloseInfo(
            dayOfWeek = info.dayOfWeek.value,
            opensByDayType = info.opensByDayType.map(CachedOpenCloseTimeInfo::from)
        )
    }
}

@Serializable
data class CachedOpenCloseTimeInfo(
    val type: String,
    val openTime: String,
    val closeTime: String
) {
    fun toOpenCloseTimeInfo(): OpenCloseTimeInfo = OpenCloseTimeInfo(type, openTime, closeTime)

    companion object {
        fun from(info: OpenCloseTimeInfo): CachedOpenCloseTimeInfo =
            CachedOpenCloseTimeInfo(info.type, info.openTime, info.closeTime)
    }
}

private fun String.toCoopShopDayTypeOrLegacyName(): CoopShopDayType = when (this) {
    "Weekday" -> CoopShopDayType.Weekday
    "Weekend" -> CoopShopDayType.Weekend
    "Monday" -> CoopShopDayType.Monday
    "Tuesday" -> CoopShopDayType.Tuesday
    "Wednesday" -> CoopShopDayType.Wednesday
    "Thursday" -> CoopShopDayType.Thursday
    "Friday" -> CoopShopDayType.Friday
    "Saturday" -> CoopShopDayType.Saturday
    "Sunday" -> CoopShopDayType.Sunday
    else -> toCoopShopDayType
}
