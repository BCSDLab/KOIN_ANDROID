package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

sealed class RegisterMenuSideEffect {
}

sealed class ImageHolder{
    data object TempUri: ImageHolder()
    data class ImageUri(val imageUri: String): ImageHolder()
}

sealed class PriceHolder{
    data object TempPrice: PriceHolder()
    data class PriceString(val priceString: String): PriceHolder()
}