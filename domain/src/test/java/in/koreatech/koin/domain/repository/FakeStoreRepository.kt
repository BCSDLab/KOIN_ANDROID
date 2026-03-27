package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.CartItemsCount
import `in`.koreatech.koin.domain.model.store.CartPaymentSummary
import `in`.koreatech.koin.domain.model.store.CartSummary
import `in`.koreatech.koin.domain.model.store.OrderHistoryRelated
import `in`.koreatech.koin.domain.model.store.OrderInProgress
import `in`.koreatech.koin.domain.model.store.OrderableShopSearchRelated
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.model.store.ReviewDetail
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
import `in`.koreatech.koin.domain.model.store.StoreWithMenuV2

class FakeStoreRepository : StoreRepository {
    private var fakeShops: List<Shop> = emptyList()

    fun setFakeShops(shops: List<Shop>) {
        fakeShops = shops
    }

    override suspend fun getStores(storeSorter: StoreSorter?, isOperating: Boolean?, isDelivery: Boolean?, query: String?): List<Store> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreEvents(): List<StoreEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreCategories(): List<StoreCategories> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreWithMenuV2(storeId: Int): StoreWithMenuV2 {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopMenus(storeId: Int): StoreMenu {
        TODO("Not yet implemented")
    }

    override suspend fun getShopEvents(storeId: Int): ShopEvents {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreReviews(storeId: Int): StoreReview {
        TODO("Not yet implemented")
    }

    override suspend fun invalidateStores() {
        TODO("Not yet implemented")
    }

    override suspend fun writeReview(shopId: Int, content: Review) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(reviewId: Int, shopId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyReview(reviewId: Int, shopId: Int, content: Review) {
        TODO("Not yet implemented")
    }

    override suspend fun searchReview(reviewId: Int, shopId: Int): ReviewDetail {
        TODO("Not yet implemented")
    }

    override suspend fun reportReview(storeId: Int?, reviewId: Int?, reportList: List<StoreReport>?): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreBenefitShopList(uid: Int): StoreBenefit {
        TODO("Not yet implemented")
    }

    override suspend fun getStoreBenefitCategories(): BenefitCategoryList {
        TODO("Not yet implemented")
    }

    override suspend fun getShopSearchRelatedList(query: String): ShopSearchRelatedList {
        TODO("Not yet implemented")
    }

    override suspend fun getShopSearchRelatedListV2(keyword: String): Result<OrderableShopSearchRelated> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShops(sorter: String?, filter: List<String>, categoryFilter: Int?, minimumOrderAmount: Int?): Result<List<Shop>> = Result.success(fakeShops)

    override suspend fun getNearbyShops(): Result<List<Shop>> = Result.success(fakeShops)

    override suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): Result<ShopMenu> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderableShopSearchRelated(query: String): Result<OrderableShopSearchRelated> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderHistories(page: Int?, limit: Int?, period: String?, status: String?, type: String?, query: String?): Result<OrderHistoryRelated> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartItem(cartMenuItemId: Int, cartItem: CartItem): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartItemQuantity(cartMenuItemId: Int, quantity: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addCartItem(cartAdd: CartAdd): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartItems(type: String): Result<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun validateCartItems(orderType: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetCart(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartItemsCount(): Result<CartItemsCount> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderInProgress(): Result<List<OrderInProgress>> {
        TODO("Not yet implemented")
    }
}
