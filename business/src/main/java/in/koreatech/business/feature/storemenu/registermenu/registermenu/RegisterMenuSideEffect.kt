package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import `in`.koreatech.business.feature.insertstore.insertmaininfo.BasicInfoErrorType
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenSideEffect

sealed class RegisterMenuSideEffect {

    object GoToCheckMenuScreen: RegisterMenuSideEffect()

    object FinishRegisterMenu: RegisterMenuSideEffect()
    data class ShowMessage(val type: RegisterMenuErrorType): RegisterMenuSideEffect()
}
enum class RegisterMenuErrorType {
    NullMenuName,
    NullMenuPrice,
    NullMenuCategory,
    NullMenuDescription,
    NullMenuImage,
    FailUploadImage,
    FailRegisterMenu
}

sealed class PriceHolder{
    data object TempPrice: PriceHolder()
    data class PriceString(val priceString: String): PriceHolder()
}