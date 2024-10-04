package `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu

import android.net.Uri
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice

data class ModifyMenuState(
    val shopId: Int = 0,
    val menuId: Int = 0,
    val menuName: String = "",
    val menuPrice: String = "",
    val menuOptionPrice: List<StoreMenuOptionPrice> = emptyList(),
    val menuCategory: List<StoreMenuCategory> = emptyList(),
    val menuCategoryId: List<Int> = emptyList(),
    val description: String = "",
    val imageUriList: List<String> = emptyList(),
    val imageUrlList: List<String> = emptyList(),
    val isModify: Boolean = false,
    val menuCategoryLabel: String ="",
    val imageIndex: Int = 0

)