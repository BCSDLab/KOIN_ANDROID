package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenSideEffect

sealed class InsertDetailInfoScreenSideEffect {

    data class ShowMessage(val type: DetailInfoErrorType): InsertDetailInfoScreenSideEffect()

    data class NavigateToInsertDetailInfoScreen(val storeBasicInfo: InsertDetailInfoScreenSideEffect) :  InsertDetailInfoScreenSideEffect()
}

enum class DetailInfoErrorType {
    NullStoreName,
    NullStoreAddress,
    NullStoreImage
}