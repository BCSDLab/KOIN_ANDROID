package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.net.Uri
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice

data class RegisterMenuState(

    val shopId: Int = 163,
    val menuName: String = "",
    val menuPrice: String = "",
    val menuOptionPrice: List<StoreMenuOptionPrice> = emptyList(),
    val menuCategory: List<StoreMenuCategory> = emptyList(),
    val menuCategoryId: List<Int> = emptyList(),
    val description: String = "",
    val imageUriList: List<Uri> = emptyList(),
    val imageUrlList: List<String> = emptyList(),
    val isModify: Boolean = false,
    val menuCategoryLabel: String ="",
    val imageIndex: Int = 0

)