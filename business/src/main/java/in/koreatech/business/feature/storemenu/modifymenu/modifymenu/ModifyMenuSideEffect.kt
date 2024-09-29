package `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu

sealed class ModifyMenuSideEffect {

    object GoToCheckMenuScreen: ModifyMenuSideEffect()

    object FinishModifyMenu: ModifyMenuSideEffect()
    data class ShowMessage(val type: ModifyMenuErrorType): ModifyMenuSideEffect()
}
enum class ModifyMenuErrorType {
    NullMenuName,
    NullMenuPrice,
    NullMenuCategory,
    NullMenuDescription,
    NullMenuImage,
    FailUploadImage,
    FailModifyMenu
}

sealed class ImageHolder{
    data object TempUri: ImageHolder()
}

sealed class PriceHolder{
    data object TempPrice: PriceHolder()
    data class PriceString(val priceString: String): PriceHolder()
}