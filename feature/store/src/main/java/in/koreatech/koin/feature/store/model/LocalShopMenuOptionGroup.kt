package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.ShopMenu
import `in`.koreatech.koin.domain.model.store.ShopMenu.ShopMenuPrice
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalShopMenuOptionGroup(
    val id: Int,
    val name: String,
    val description: String,
    val isRequired: Boolean,
    val minSelect: Int,
    val maxSelect: Int,
    val options: List<LocalShopMenuOption>
) : Parcelable {
    @Parcelize
    data class LocalShopMenuOption(
        val id: Int,
        val name: String,
        val price: Int,
        val optionSelected: Boolean
    ) : Parcelable
}

fun ShopMenu.ShopMenuOptionGroup.toLocalShopMenuOptionGroup(): LocalShopMenuOptionGroup {
    return LocalShopMenuOptionGroup(
        id = id,
        name = name,
        description = description,
        isRequired = isRequired,
        minSelect = minSelect,
        maxSelect = maxSelect,
        options = options.map { it.toLocalShopMenuOption() }
    )
}

fun ShopMenuPrice.toLocalShopMenuOption(): LocalShopMenuOptionGroup.LocalShopMenuOption {
    return LocalShopMenuOptionGroup.LocalShopMenuOption(
        id = id,
        name = name ?: "",
        price = price,
        optionSelected = false
    )
}
