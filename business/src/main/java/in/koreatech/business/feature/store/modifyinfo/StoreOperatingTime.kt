package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours

data class StoreOperatingTime(
    val operatingTime: OperatingTime = OperatingTime(FullHours(0, 0), FullHours(0, 0)),
    val closed: Boolean = false,
    val dayOfWeek: String = "",
    val dayOfWeekEnglish: String = ""
)