package `in`.koreatech.koin.feature.store.origin

import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel

data class ShopOriginState(
    val cartItemCount: Int = 0,
    val isOrderableShop: Boolean = true,
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val shopDescription: StoreDescriptionModel = StoreDescriptionModel.empty()
)
