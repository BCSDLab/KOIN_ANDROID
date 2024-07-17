package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory

data class RegisterMenuState(

    val shopId: Int = -1,
    val menuName: String = "",
    val menuPrice: List<String> = emptyList(),
    val menuCategory: List<StoreMenuCategory> = emptyList(),
    val description: String = "",
    val imageUriList: List<String> = emptyList(),
    val isRecommendMenu : Boolean = false,
    val isMainMenu: Boolean = false,
    val isSetMenu: Boolean = false,
    val isSideMenu: Boolean = false

)