package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.net.Uri
import android.os.Parcelable
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsertDetailInfoScreenState (
    val storeCategory: Int = -1,
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImage: Uri = Uri.EMPTY,
    val storePhoneNumber: String = "",
    val storeDeliveryFee: String ="",
    val storeOtherInfo: String = "",
    val isDeliveryOk: Boolean = false,
    val isCardOk: Boolean = false,
    val isBankOk: Boolean = false,
    val operatingTimeList: List<OperatingTimeState> = listOf(
        OperatingTimeState("00:00", false, "월", "00:00"),
        OperatingTimeState("00:00", false, "화", "00:00"),
        OperatingTimeState("00:00", false, "수", "00:00"),
        OperatingTimeState("00:00", false, "목", "00:00"),
        OperatingTimeState("00:00", false, "금", "00:00"),
        OperatingTimeState("00:00", false, "토", "00:00"),
        OperatingTimeState("00:00", false, "일", "00:00"),
    ),
    val isDetailInfoValid: Boolean = false,
    val showDialog: Boolean = false,
    val isOpenTimeSetting:Boolean = false,
    val dayOfWeekIndex: Int = -1
): Parcelable