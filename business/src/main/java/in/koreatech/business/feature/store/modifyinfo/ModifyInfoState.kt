package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.koin.domain.model.owner.SettingTime
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl

data class ModifyInfoState(
    val storeInfo: StoreDetailInfo = StoreDetailInfo(
        address = "",
        mainCategoryId = 0,
        categoryIds = listOf(),
        isDeliveryOk = false,
        deliveryPrice = 0,
        description = "",
        imageUrls = listOf(),
        name = "",
        operatingTime = listOf(),
        isBankOk = false,
        isCardOk = false,
        phone = "",
        bank = "",
        accountNumber = "",
    ),
    val operatingTimeList: List<OperatingTime> = listOf(),
    val fileInfo: MutableList<StoreUrl> = mutableListOf(),
    val showDialog: Boolean = false,
    val dayOfWeekIndex: Int = -1,
    val isOpenTimeSetting: SettingTime = SettingTime.OPEN,
)
