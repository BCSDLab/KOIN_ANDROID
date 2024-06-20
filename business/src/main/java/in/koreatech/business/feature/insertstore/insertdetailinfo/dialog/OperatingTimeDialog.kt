package `in`.koreatech.business.feature.insertstore.insertdetailinfo.dialog

import com.chargemap.compose.numberpicker.Hours

data class OperatingTimeDialog(
    val showDialog: Boolean = true,
    val closeDialog: () -> Unit = {},
    val dayOfWeekIndex: Int = 0,
    val onSettingStoreTime: (Pair<Hours, Int>) -> Unit = {}
)