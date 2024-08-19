package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.Hours

data class OperatingTime(
    val openTime:Hours,
    val closeTime:Hours
)