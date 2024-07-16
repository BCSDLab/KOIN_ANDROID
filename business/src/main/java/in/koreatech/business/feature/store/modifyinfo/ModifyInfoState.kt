package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl

data class ModifyInfoState(
    val storeInfo: StoreDetailInfo = StoreDetailInfo(
        address = "",
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
    val fileInfo: MutableList<StoreUrl> = mutableListOf(),
    val dialogTimeState: OperatingTime = OperatingTime(FullHours(0, 0), FullHours(0, 0)),
    val showDialog: Boolean = false,
    val dayOfWeekIndex: Int = -1,
)
