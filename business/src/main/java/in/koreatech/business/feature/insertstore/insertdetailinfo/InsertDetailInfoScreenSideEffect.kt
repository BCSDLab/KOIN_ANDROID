package `in`.koreatech.business.feature.insertstore.insertdetailinfo

sealed class InsertDetailInfoScreenSideEffect {

    data class ShowMessage(val type: DetailInfoErrorType): InsertDetailInfoScreenSideEffect()

    data class NavigateToCheckScreen(val storeDetailInfo: InsertDetailInfoScreenState) :  InsertDetailInfoScreenSideEffect()
}

enum class DetailInfoErrorType {
    NullStorePhoneNumber,
    NullStoreDeliveryFee,
    NullStoreOtherInfo,
}