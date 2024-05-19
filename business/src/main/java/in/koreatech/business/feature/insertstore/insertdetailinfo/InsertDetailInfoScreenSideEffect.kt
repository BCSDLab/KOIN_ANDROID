package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenSideEffect

sealed class InsertDetailInfoScreenSideEffect {

    data class ShowMessage(val type: ErrorType): InsertDetailInfoScreenSideEffect()

    data class NavigateToInsertDetailInfoScreen(val storeBasicInfo: InsertDetailInfoScreenSideEffect) :  InsertDetailInfoScreenSideEffect()
}

enum class ErrorType {
    NullStoreName,
    NullStoreAddress,
    NullStoreImage
}