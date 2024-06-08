package `in`.koreatech.business.feature.insertstore.insertmaininfo

import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo


sealed class InsertBasicInfoScreenSideEffect {

    data class ShowMessage(val type: BasicInfoErrorType): InsertBasicInfoScreenSideEffect()
    data class NavigateToInsertDetailInfoScreen(val storeBasicInfo: InsertBasicInfoScreenState) : InsertBasicInfoScreenSideEffect()
}

enum class BasicInfoErrorType {
    NullStoreName,
    NullStoreAddress,
    NullStoreImage
}