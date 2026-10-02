package `in`.koreatech.koin.feature.store.detail

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.ShopInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class StoreDetailState(
    val store: ShopInfoModel = ShopInfoModel.empty(),
    val isOrderableShop: Boolean = true,
    val shopDescription: StoreDescriptionModel = StoreDescriptionModel.empty(),
    val categories: ImmutableList<MenuCategoryModel> = persistentListOf(),
    val storeReview: StoreReview = StoreReview.empty(),
    val selectedCategoryId: Int = 0,
    val isLogin: Boolean = false,
    val isLoading: Boolean = true,
    val storeId: Int = -1,
    val cartItemCount: Int = 0,
    val showSignInDialog: Boolean = false,
    val minimumOrderAmount: Int = 0,
    val cart: Cart = Cart.Empty,
    val cartType: CartType = CartType.DELIVERY,
    val cartValidation: CartValidation = CartValidation.NONE,
    val showCallDialog: Boolean = false,
    val showImageDialog: Boolean = false,
    val noticePreview: String? = null
)
fun OwnerInfoModel?.hasAnyInfo(): Boolean {
    return this?.let {
        it.name != null ||
            it.shopName != null ||
            it.address != null ||
            it.companyRegistrationNumber != null
    } ?: false
}
