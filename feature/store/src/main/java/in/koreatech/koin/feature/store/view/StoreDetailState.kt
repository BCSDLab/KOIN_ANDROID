package `in`.koreatech.koin.feature.store.view

import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.ShopInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel

data class StoreDetailState(
    val store: ShopInfoModel = ShopInfoModel.empty(),
    val isOrderableShop: Boolean = true,
    val shopDescription: StoreDescriptionModel = StoreDescriptionModel.empty(),
    val orderableStore: OrderShop = OrderShop.empty(),
    val categories: List<MenuCategoryModel> = emptyList(),
    val storeMenu: List<ShopMenus> = emptyList(),
    val storeReview: StoreReview = StoreReview.empty(),
    val scrollUp: StoreDetailScrollType = StoreDetailScrollType.NONE,
    val availableDelivery: Boolean = false,
    val isLogin: Boolean = false,
    val isLoading: Boolean = true,
    val cartItemCount: Int = 0
)
fun OwnerInfoModel?.hasAnyInfo(): Boolean {
    return this?.let {
        it.name != null ||
            it.shopName != null ||
            it.address != null ||
            it.companyRegistrationNumber != null
    } ?: false
}
