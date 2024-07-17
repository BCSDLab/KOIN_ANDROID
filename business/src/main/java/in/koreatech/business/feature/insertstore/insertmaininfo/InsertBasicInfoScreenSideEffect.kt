package `in`.koreatech.business.feature.insertstore.insertmaininfo
sealed class InsertBasicInfoScreenSideEffect {

    data class ShowMessage(val type: BasicInfoErrorType): InsertBasicInfoScreenSideEffect()
    data class NavigateToInsertDetailInfoScreen(val storeBasicInfo: InsertBasicInfoScreenState) : InsertBasicInfoScreenSideEffect()
}

enum class BasicInfoErrorType {
    NullStoreName,
    NullStoreAddress,
    NullStoreImage,
    FailUploadImage
}