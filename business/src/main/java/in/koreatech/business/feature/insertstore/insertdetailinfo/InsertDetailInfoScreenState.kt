package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.os.Parcelable
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsertDetailInfoScreenState(
    val storeCategory: Int = -1,
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImage: String = "",
    val storePhoneNumber: String = "",
    val storeDeliveryFee: String = "",
    val storeOtherInfo: String = "",
    val isDeliveryOk: Boolean = false,
    val isCardOk: Boolean = false,
    val isBankOk: Boolean = false,
    val operatingTimeList: List<OperatingTimeState> = emptyList(),
    val isDetailInfoValid: Boolean = false,
    val settingTimeInfoList: List<TimeSettingState> = emptyList(),
) : Parcelable
