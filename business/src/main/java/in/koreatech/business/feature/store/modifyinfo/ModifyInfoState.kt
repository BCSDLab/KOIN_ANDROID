package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours

data class ModifyInfoState(
    val operatingTimeList: List<StoreOperatingTime> = listOf(
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "월", "MONDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "화", "TUESDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "수", "WEDNESDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "목", "THURSDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "금", "FRIDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "토", "SATURDAY"),
        StoreOperatingTime(OperatingTime(FullHours(0, 0), FullHours(0, 0)), false, "일", "SUNDAY"),
    ),
    val dialogTimeState: OperatingTime = OperatingTime(FullHours(0, 0), FullHours(0, 0)),
    val showDialog: Boolean = false,
    val dayOfWeekIndex: Int = -1,
)
