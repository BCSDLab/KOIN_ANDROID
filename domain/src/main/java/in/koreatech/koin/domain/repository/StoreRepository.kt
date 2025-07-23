package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.CartPaymentSummary
import `in`.koreatech.koin.domain.model.store.CartSummary
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.ShopDeliveryAvailable
import `in`.koreatech.koin.domain.model.store.ShopDetail
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.ShopMenu
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.ShopMenusGroup
import `in`.koreatech.koin.domain.model.store.ShopSearchRelatedList
import `in`.koreatech.koin.domain.model.store.ShopSummary
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreBenefit
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreReport
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface StoreRepository {
    suspend fun getStores(
        storeSorter: StoreSorter? = null,
        isOperating: Boolean? = null,
        isDelivery: Boolean? = null,
        query: String? = null
    ): List<Store>

    suspend fun getStoreEvents(): List<StoreEvent>

    suspend fun getStoreCategories(): List<StoreCategories>

    suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu

    suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory>

    suspend fun getShopMenus(storeId: Int): StoreMenu

    suspend fun getShopEvents(storeId: Int): ShopEvents

    suspend fun getStoreReviews(storeId: Int): StoreReview

    suspend fun invalidateStores()

    suspend fun writeReview(
        shopId: Int,
        content: Review
    )

    suspend fun deleteReview(
        reviewId: Int,
        shopId: Int
    )

    suspend fun modifyReview(
        reviewId: Int,
        shopId: Int,
        content: Review
    )

    suspend fun reportReview(
        storeId: Int?,
        reviewId: Int?,
        reportList: List<StoreReport>?
    ): Result<Unit>

    suspend fun getStoreBenefitShopList(uid: Int): StoreBenefit

    suspend fun getStoreBenefitCategories(): BenefitCategoryList

    suspend fun getShopSearchRelatedList(query: String): ShopSearchRelatedList

    suspend fun getOrderableShops(): Result<List<Shop>>

    suspend fun getNearbyShops(): Result<List<Shop>>

    suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary>

    suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail>

    suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable>

    suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>>

    suspend fun getOrderableShopMenu(
        shopId: Int,
        menuId: Int
    ): Result<CartItemEdit>

    suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>>

    suspend fun updateCartItem(
        cartMenuItemId: Int,
        cartItem: CartItem
    ): Result<Unit>

    suspend fun updateCartItemQuantity(
        cartMenuItemId: Int,
        quantity: Int
    ): Result<Unit>

    suspend fun addCartItem(cartAdd: CartAdd): Result<Unit>

    suspend fun getCartItems(type: String): Result<Cart>

    suspend fun validateCartItems(): Result<Unit>

    suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary>

    suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary>

    suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit>

    suspend fun resetCart(): Result<Unit>

    suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit>
}
