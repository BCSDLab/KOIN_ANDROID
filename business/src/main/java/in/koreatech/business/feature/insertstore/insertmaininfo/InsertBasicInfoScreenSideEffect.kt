package `in`.koreatech.business.feature.insertstore.insertmaininfo

import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo


sealed class InsertBasicInfoScreenSideEffect {

    data class ShowMessage(val type: ErrorType): InsertBasicInfoScreenSideEffect()

    data class NavigateToInsertDetailInfoScreen(val storeBasicInfo: StoreBasicInfo) : InsertBasicInfoScreenSideEffect()
}

enum class ErrorType {
    NullStoreName,
    NullStoreAddress,
    NullStoreImage
}