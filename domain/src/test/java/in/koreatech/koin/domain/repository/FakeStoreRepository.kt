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
    private var fakeStores: List<Store> = emptyList()
    private var fakeStoreEvents: List<StoreEvent> = emptyList()
    private var fakeStoreCategories: List<StoreCategories> = emptyList()
    private var fakeStoreWithMenu: StoreWithMenu? = null
    private var fakeStoreWithMenuV2: StoreWithMenuV2? = null
    private var fakeStoreMenuCategories: List<StoreMenuCategory> = emptyList()
    private var fakeStoreMenu: StoreMenu? = null
    private var fakeShopEvents: ShopEvents? = null
    private var fakeStoreReview: StoreReview? = null
    private var fakeReviewDetail: ReviewDetail? = null
    private var fakeStoreBenefit: StoreBenefit? = null
    private var fakeBenefitCategoryList: BenefitCategoryList? = null
    private var fakeShopSearchRelatedList: ShopSearchRelatedList? = null
    private var fakeOrderableShopSearchRelated: OrderableShopSearchRelated? = null
    private var fakeShopSummary: ShopSummary? = null
    private var fakeShopDetail: ShopDetail? = null
    private var fakeShopDeliveryAvailable: ShopDeliveryAvailable? = null
    private var fakeShopMenusList: List<ShopMenus> = emptyList()
    private var fakeShopMenu: ShopMenu? = null
    private var fakeShopMenusGroups: List<ShopMenusGroup> = emptyList()
    private var fakeOrderHistoryRelated: OrderHistoryRelated? = null
    private var fakeCart: Cart? = null
    private var fakeCartSummary: CartSummary? = null
    private var fakeCartPaymentSummary: CartPaymentSummary? = null
    private var fakeCartItemEdit: CartItemEdit? = null
    private var fakeCartItemsCount: CartItemsCount? = null
    private var fakeOrdersInProgress: List<OrderInProgress> = emptyList()

    var lastCapturedMinimumOrderAmount: Int? = Int.MIN_VALUE

    fun setFakeShops(shops: List<Shop>) {
        fakeShops = shops
    }
    fun setFakeStores(stores: List<Store>) {
        fakeStores = stores
    }
    fun setFakeStoreEvents(events: List<StoreEvent>) {
        fakeStoreEvents = events
    }
    fun setFakeStoreCategories(categories: List<StoreCategories>) {
        fakeStoreCategories = categories
    }
    fun setFakeStoreWithMenu(storeWithMenu: StoreWithMenu) {
        fakeStoreWithMenu = storeWithMenu
    }
    fun setFakeStoreWithMenuV2(storeWithMenuV2: StoreWithMenuV2) {
        fakeStoreWithMenuV2 = storeWithMenuV2
    }
    fun setFakeStoreMenuCategories(categories: List<StoreMenuCategory>) {
        fakeStoreMenuCategories = categories
    }
    fun setFakeStoreMenu(storeMenu: StoreMenu) {
        fakeStoreMenu = storeMenu
    }
    fun setFakeShopEvents(shopEvents: ShopEvents) {
        fakeShopEvents = shopEvents
    }
    fun setFakeStoreReview(storeReview: StoreReview) {
        fakeStoreReview = storeReview
    }
    fun setFakeReviewDetail(reviewDetail: ReviewDetail) {
        fakeReviewDetail = reviewDetail
    }
    fun setFakeStoreBenefit(storeBenefit: StoreBenefit) {
        fakeStoreBenefit = storeBenefit
    }
    fun setFakeBenefitCategoryList(list: BenefitCategoryList) {
        fakeBenefitCategoryList = list
    }
    fun setFakeShopSearchRelatedList(list: ShopSearchRelatedList) {
        fakeShopSearchRelatedList = list
    }
    fun setFakeOrderableShopSearchRelated(related: OrderableShopSearchRelated) {
        fakeOrderableShopSearchRelated = related
    }
    fun setFakeShopSummary(summary: ShopSummary) {
        fakeShopSummary = summary
    }
    fun setFakeShopDetail(detail: ShopDetail) {
        fakeShopDetail = detail
    }
    fun setFakeShopDeliveryAvailable(delivery: ShopDeliveryAvailable) {
        fakeShopDeliveryAvailable = delivery
    }
    fun setFakeShopMenusList(menus: List<ShopMenus>) {
        fakeShopMenusList = menus
    }
    fun setFakeShopMenu(menu: ShopMenu) {
        fakeShopMenu = menu
    }
    fun setFakeShopMenusGroups(groups: List<ShopMenusGroup>) {
        fakeShopMenusGroups = groups
    }
    fun setFakeOrderHistoryRelated(history: OrderHistoryRelated) {
        fakeOrderHistoryRelated = history
    }
    fun setFakeCart(cart: Cart) {
        fakeCart = cart
    }
    fun setFakeCartSummary(summary: CartSummary) {
        fakeCartSummary = summary
    }
    fun setFakeCartPaymentSummary(summary: CartPaymentSummary) {
        fakeCartPaymentSummary = summary
    }
    fun setFakeCartItemEdit(edit: CartItemEdit) {
        fakeCartItemEdit = edit
    }
    fun setFakeCartItemsCount(count: CartItemsCount) {
        fakeCartItemsCount = count
    }
    fun setFakeOrdersInProgress(orders: List<OrderInProgress>) {
        fakeOrdersInProgress = orders
    }

    override suspend fun getStores(storeSorter: StoreSorter?, isOperating: Boolean?, isDelivery: Boolean?, query: String?): List<Store> =
        fakeStores

    override suspend fun getStoreEvents(): List<StoreEvent> = fakeStoreEvents

    override suspend fun getStoreCategories(): List<StoreCategories> = fakeStoreCategories

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu =
        fakeStoreWithMenu ?: throw IllegalStateException("No StoreWithMenu set")

    override suspend fun getStoreWithMenuV2(storeId: Int): StoreWithMenuV2 =
        fakeStoreWithMenuV2 ?: throw IllegalStateException("No StoreWithMenuV2 set")

    override suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory> =
        fakeStoreMenuCategories

    override suspend fun getShopMenus(storeId: Int): StoreMenu =
        fakeStoreMenu ?: throw IllegalStateException("No StoreMenu set")

    override suspend fun getShopEvents(storeId: Int): ShopEvents =
        fakeShopEvents ?: throw IllegalStateException("No ShopEvents set")

    override suspend fun getStoreReviews(storeId: Int): StoreReview =
        fakeStoreReview ?: throw IllegalStateException("No StoreReview set")

    override suspend fun invalidateStores() {}

    override suspend fun writeReview(shopId: Int, content: Review) {}

    override suspend fun deleteReview(reviewId: Int, shopId: Int): Result<Unit> =
        Result.success(Unit)

    override suspend fun modifyReview(reviewId: Int, shopId: Int, content: Review) {}

    override suspend fun searchReview(reviewId: Int, shopId: Int): ReviewDetail =
        fakeReviewDetail ?: throw IllegalStateException("No ReviewDetail set")

    override suspend fun reportReview(storeId: Int?, reviewId: Int?, reportList: List<StoreReport>?): Result<Unit> =
        Result.success(Unit)

    override suspend fun getStoreBenefitShopList(uid: Int): StoreBenefit =
        fakeStoreBenefit ?: throw IllegalStateException("No StoreBenefit set")

    override suspend fun getStoreBenefitCategories(): BenefitCategoryList =
        fakeBenefitCategoryList ?: throw IllegalStateException("No BenefitCategoryList set")

    override suspend fun getShopSearchRelatedList(query: String): ShopSearchRelatedList =
        fakeShopSearchRelatedList ?: throw IllegalStateException("No ShopSearchRelatedList set")

    override suspend fun getShopSearchRelatedListV2(keyword: String): Result<OrderableShopSearchRelated> =
        fakeOrderableShopSearchRelated?.let { Result.success(it) }
            ?: throw IllegalStateException("No OrderableShopSearchRelated set")

    override suspend fun getOrderableShops(sorter: String?, filter: List<String>, categoryFilter: Int?, minimumOrderAmount: Int?): Result<List<Shop>> {
        lastCapturedMinimumOrderAmount = minimumOrderAmount
        return Result.success(fakeShops)
    }

    override suspend fun getNearbyShops(): Result<List<Shop>> = Result.success(fakeShops)

    override suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary> =
        fakeShopSummary?.let { Result.success(it) }
            ?: throw IllegalStateException("No ShopSummary set")

    override suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail> =
        fakeShopDetail?.let { Result.success(it) }
            ?: throw IllegalStateException("No ShopDetail set")

    override suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable> =
        fakeShopDeliveryAvailable?.let { Result.success(it) }
            ?: throw IllegalStateException("No ShopDeliveryAvailable set")

    override suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>> =
        Result.success(fakeShopMenusList)

    override suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): Result<ShopMenu> =
        fakeShopMenu?.let { Result.success(it) }
            ?: throw IllegalStateException("No ShopMenu set")

    override suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>> =
        Result.success(fakeShopMenusGroups)

    override suspend fun getOrderableShopSearchRelated(query: String): Result<OrderableShopSearchRelated> =
        fakeOrderableShopSearchRelated?.let { Result.success(it) }
            ?: throw IllegalStateException("No OrderableShopSearchRelated set")

    override suspend fun getOrderHistories(page: Int?, limit: Int?, period: String?, status: String?, type: String?, query: String?): Result<OrderHistoryRelated> =
        fakeOrderHistoryRelated?.let { Result.success(it) }
            ?: throw IllegalStateException("No OrderHistoryRelated set")

    override suspend fun updateCartItem(cartMenuItemId: Int, cartItem: CartItem): Result<Unit> =
        Result.success(Unit)

    override suspend fun updateCartItemQuantity(cartMenuItemId: Int, quantity: Int): Result<Unit> =
        Result.success(Unit)

    override suspend fun addCartItem(cartAdd: CartAdd): Result<Unit> = Result.success(Unit)

    override suspend fun getCartItems(type: String): Result<Cart> =
        fakeCart?.let { Result.success(it) }
            ?: throw IllegalStateException("No Cart set")

    override suspend fun validateCartItems(orderType: String): Result<Unit> = Result.success(Unit)

    override suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary> =
        fakeCartSummary?.let { Result.success(it) }
            ?: throw IllegalStateException("No CartSummary set")

    override suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary> =
        fakeCartPaymentSummary?.let { Result.success(it) }
            ?: throw IllegalStateException("No CartPaymentSummary set")

    override suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit> =
        fakeCartItemEdit?.let { Result.success(it) }
            ?: throw IllegalStateException("No CartItemEdit set")

    override suspend fun resetCart(): Result<Unit> = Result.success(Unit)

    override suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit> = Result.success(Unit)

    override suspend fun getCartItemsCount(): Result<CartItemsCount> =
        fakeCartItemsCount?.let { Result.success(it) }
            ?: throw IllegalStateException("No CartItemsCount set")

    override suspend fun getOrderInProgress(): Result<List<OrderInProgress>> =
        Result.success(fakeOrdersInProgress)
}
