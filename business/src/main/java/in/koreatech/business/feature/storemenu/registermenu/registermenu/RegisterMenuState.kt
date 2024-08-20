package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.net.Uri
import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory

data class RegisterMenuState(

    val shopId: Int = -1,
    val menuName: String = "",
    val menuPrice: String = "",
    val menuDetailPrice: List<Pair<String, String>> = emptyList(),
    val menuCategory: List<StoreMenuCategory> = emptyList(),
    val description: String = "",
    val imageUriList: List<Uri> = emptyList(),
    val isRecommendMenu : Boolean = false,
    val isMainMenu: Boolean = false,
    val isSetMenu: Boolean = false,
    val isSideMenu: Boolean = false,
    val isModify: Boolean = false,
    val imageIndex: Int = 0

)