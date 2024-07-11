package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState

data class ModifyInfoState(
    val operatingTimeList: List<OperatingTimeState> = listOf(
        OperatingTimeState("00:00", false, "월", "00:00", "MONDAY"),
        OperatingTimeState("00:00", false, "화", "00:00", "TUESDAY"),
        OperatingTimeState("00:00", false, "수", "00:00", "WEDNESDAY"),
        OperatingTimeState("00:00", false, "목", "00:00", "THURSDAY"),
        OperatingTimeState("00:00", false, "금", "00:00", "FRIDAY"),
        OperatingTimeState("00:00", false, "토", "00:00", "SATURDAY"),
        OperatingTimeState("00:00", false, "일", "00:00", "SUNDAY"),
    ),
    val dialogTimeState: OperatingTime = OperatingTime(FullHours(0, 0), FullHours(0, 0)),
    val showDialog: Boolean = false,
    val dayOfWeekIndex: Int = -1,
)
