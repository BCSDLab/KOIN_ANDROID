package `in`.koreatech.business.feature.store.modifyinfo

import com.chargemap.compose.numberpicker.FullHours
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo

data class ModifyInfoState(
    val storeInfo: StoreDetailInfo? = null,
    val dialogTimeState: OperatingTime = OperatingTime(FullHours(0, 0), FullHours(0, 0)),
    val showDialog: Boolean = false,
    val dayOfWeekIndex: Int = -1,
)
