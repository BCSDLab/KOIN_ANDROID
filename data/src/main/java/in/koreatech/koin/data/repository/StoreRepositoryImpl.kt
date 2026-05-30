package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.mapper.toCart
import `in`.koreatech.koin.data.mapper.toCartAddRequest
import `in`.koreatech.koin.data.mapper.toCartItemEdit
import `in`.koreatech.koin.data.mapper.toCartItemRequest
import `in`.koreatech.koin.data.mapper.toCartItemsCount
import `in`.koreatech.koin.data.mapper.toCartPaymentSummary
import `in`.koreatech.koin.data.mapper.toCartSummary
import `in`.koreatech.koin.data.mapper.toCategory
import `in`.koreatech.koin.data.mapper.toOrderHistoryRelated
import `in`.koreatech.koin.data.mapper.toOrderInProgress
import `in`.koreatech.koin.data.mapper.toOrderableShopSearchRelated
import `in`.koreatech.koin.data.mapper.toReviewDetail
import `in`.koreatech.koin.data.mapper.toShop
import `in`.koreatech.koin.data.mapper.toShopDeliveryAvailable
import `in`.koreatech.koin.data.mapper.toShopDetail
import `in`.koreatech.koin.data.mapper.toShopMenu
import `in`.koreatech.koin.data.mapper.toShopMenus
import `in`.koreatech.koin.data.mapper.toShopMenusGroup
import `in`.koreatech.koin.data.mapper.toShopSearchRelatedList
import `in`.koreatech.koin.data.mapper.toShopSummary
import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreBenefitCategory
import `in`.koreatech.koin.data.mapper.toStoreCategories
import `in`.koreatech.koin.data.mapper.toStoreCount
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreEvent
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreReview
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.mapper.toStoreWithMenuV2
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.source.local.CacheLocalDataSource
import `in`.koreatech.koin.data.source.local.StoreLocalDataSource
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.data.util.suspendRunCatching
import `in`.koreatech.koin.domain.error.store.KoinStoreException
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
import `in`.koreatech.koin.domain.model.store.StoreCount
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreReport
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenuV2
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import retrofit2.HttpException

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
    private val storeLocalDataSource: StoreLocalDataSource,
    private val cacheLocalDataSource: CacheLocalDataSource
) : StoreRepository {
    private var stores: List<Store>? = null
    private var storeEvents: List<StoreEvent>? = null

    override suspend fun getStores(
        storeSorter: StoreSorter?,
        isOperating: Boolean?,
        isDelivery: Boolean?,
        query: String?
    ): List<Store> {
        if (stores == null) {
            stores =
                if (isOperating == true && isDelivery == true) {
                    storeRemoteDataSource.getStoreItemsWithTwoFilter(storeSorter, query).map { it.toStore() }
                } else if (isOperating == false && isDelivery == false) {
                    storeRemoteDataSource.getStoreItemsWithSorting(storeSorter, query).map { it.toStore() }
                } else {
                    if (isOperating == true) {
                        storeRemoteDataSource.getStoreItemsWithOneFilter(storeSorter, "OPEN", query).map { it.toStore() }
                    } else {
                        storeRemoteDataSource.getStoreItemsWithOneFilter(storeSorter, "DELIVERY", query).map { it.toStore() }
                    }
                }
        }

        return stores!!
    }

    override suspend fun getStoreEvents(): List<StoreEvent> {
        if (storeEvents == null) {
            storeEvents = storeRemoteDataSource.getStoreEvents().map { it.toStoreEvent() }
        }

        return storeEvents!!
    }

    override suspend fun getStoreCategories(): List<StoreCategories> {
        val cachedStoreCategories = storeLocalDataSource.getCachedStoreCategories()
        val cachedTime = cacheLocalDataSource.getCachedTime(DBConstant.STORE_CATEGORIES)
        val now = System.currentTimeMillis()

        val cacheShouldExpire = cachedTime == null || (now - cachedTime) > 7.days.inWholeMilliseconds

        val shouldFetch = cachedStoreCategories.isEmpty() || cacheShouldExpire

        return if (!shouldFetch) {
            cachedStoreCategories
        } else {
            storeRemoteDataSource.getStoreCategories().also {
                storeLocalDataSource.setCachedStoreCategories(it)
                cacheLocalDataSource.updateCachedTime(DBConstant.STORE_CATEGORIES)
            }
        }.map { it.toStoreCategories() }
    }

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu {
        return storeRemoteDataSource.getStoreMenu(storeId).toStoreWithMenu()
    }

    override suspend fun getStoreWithMenuV2(storeId: Int): StoreWithMenuV2 {
        return storeRemoteDataSource.getStoreMenuV2(storeId).toStoreWithMenuV2()
    }

    override suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory> {
        return storeRemoteDataSource.getStoreMenuCategory(storeId).toCategory()
    }

    override suspend fun getShopMenus(storeId: Int): StoreMenu {
        return storeRemoteDataSource.getShopMenus(storeId).toStoreMenu()
    }

    override suspend fun getShopEvents(storeId: Int): ShopEvents {
        return storeRemoteDataSource.getShopEvents(storeId).toStoreDetailEvents()
    }

    override suspend fun getStoreReviews(storeId: Int): StoreReview {
        return storeRemoteDataSource.getStoreReviews(storeId).toStoreReview()
    }

    override suspend fun invalidateStores() {
        stores = null
    }

    override suspend fun writeReview(
        shopId: Int,
        content: Review
    ) {
        storeRemoteDataSource.writeReview(
            shopId,
            ReviewRequest(
                content = content.content,
                rating = content.rating,
                imageUrls = content.imageUrls,
                menuNames = content.menuNames
            )
        )
    }

    override suspend fun deleteReview(
        reviewId: Int,
        shopId: Int
    ): Result<Unit> {
        return try {
            storeRemoteDataSource.deleteReview(reviewId, shopId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun modifyReview(
        reviewId: Int,
        shopId: Int,
        content: Review
    ) {
        storeRemoteDataSource.modifyReview(
            reviewId,
            shopId,
            ReviewRequest(
                content = content.content,
                rating = content.rating,
                imageUrls = content.imageUrls,
                menuNames = content.menuNames
            )
        )
    }

    override suspend fun searchReview(reviewId: Int, shopId: Int): ReviewDetail {
        return storeRemoteDataSource.searchReview(reviewId, shopId).toReviewDetail()
    }

    override suspend fun reportReview(
        storeId: Int?,
        reviewId: Int?,
        reportList: List<StoreReport>?
    ): Result<Unit> {
        return try {
            if (storeId != null && reviewId != null && reportList != null) {
                storeRemoteDataSource.postReviewReports(
                    storeId,
                    reviewId,
                    reportList
                )
            }
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStoreBenefitShopList(uid: Int): StoreBenefit {
        storeRemoteDataSource.getStoreBenefitShopList(uid).apply {
            return StoreBenefit(count ?: 0, shops?.map { it.toStore() } ?: emptyList())
        }
    }

    override suspend fun getStoreBenefitCategories(): BenefitCategoryList {
        return storeRemoteDataSource.getStoreBenefitCategories().toStoreBenefitCategory()
    }

    override suspend fun getShopSearchRelatedList(query: String): ShopSearchRelatedList {
        return storeRemoteDataSource.getShopSearchRelated(query).toShopSearchRelatedList()
    }

    override suspend fun getShopSearchRelatedListV2(keyword: String): Result<OrderableShopSearchRelated> {
        return suspendRunCatching {
            storeRemoteDataSource.getShopSearchRelatedV2(keyword).toOrderableShopSearchRelated()
        }.mapHttpFailure { }
    }

    override suspend fun getOrderableShops(
        sorter: String?,
        filter: List<String>,
        categoryFilter: Int?,
        minimumOrderAmount: Int?
    ): Result<List<Shop>> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShops(sorter, filter, categoryFilter, minimumOrderAmount).map { it.toShop() }
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getNearbyShops(): Result<List<Shop>> {
        return suspendRunCatching {
            storeLocalDataSource.getCachedNearbyShops()?.map { it.toShop() } ?: storeRemoteDataSource.getNearbyShops().shops.also {
                storeLocalDataSource.setCachedNearbyShops(it)
            }.map {
                it.toShop()
            }
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopSummary(shopId).toShopSummary()
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopDetail(shopId).toShopDetail()
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopDelivery(shopId).toShopDeliveryAvailable()
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopMenus(shopId).map { it.toShopMenus() }
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): Result<ShopMenu> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopMenu(shopId, menuId).toShopMenu()
        }.mapHttpFailure {
            on(404) throws KoinStoreException.MenuNotFoundException()
        }
    }

    override suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopMenuGroups(shopId).map { it.toShopMenusGroup() }
        }.mapHttpFailure {
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun getOrderableShopSearchRelated(query: String): Result<OrderableShopSearchRelated> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderableShopSearchRelated(query).toOrderableShopSearchRelated()
        }.mapHttpFailure { }
    }

    override suspend fun updateCartItem(cartMenuItemId: Int, cartItem: CartItem): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.updateCartItem(cartMenuItemId, cartItem.toCartItemRequest())
        }.mapHttpFailure {
            on(400, "REQUIRED_OPTION_GROUP_MISSING") throws KoinStoreException.RequiredOptionGroupMissingException()
            on(400, "MIN_SELECTION_NOT_MET") throws KoinStoreException.MinimumSelectionNotMetException()
            on(400, "MAX_SELECTION_EXCEEDED") throws KoinStoreException.MaxSelectionExceededException()
            on(400, "INVALID_OPTION_IN_GROUP") throws KoinStoreException.InvalidOptionInGroupException()
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404, "CART_MENU_ITEM_NOT_FOUND") throws KoinStoreException.CartItemNotFoundException()
            on(404, "MENU_OPTION_NOT_FOUND") throws KoinStoreException.MenuOptionNotFoundException()
            on(404, "MENU_PRICE_NOT_FOUND") throws KoinStoreException.MenuPriceNotFoundException()
        }
    }

    override suspend fun updateCartItemQuantity(cartMenuItemId: Int, quantity: Int): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.updateCartItemQuantity(cartMenuItemId, quantity)
        }.mapHttpFailure {
            on(400) throws KoinStoreException.InvalidQuantityException()
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404, "CART_MENU_ITEM_NOT_FOUND") throws KoinStoreException.CartItemNotFoundException()
            on(404, "CART_NOT_FOUND") throws KoinStoreException.CartNotFoundException()
        }
    }

    override suspend fun addCartItem(cartAdd: CartAdd): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.addCartItem(cartAdd.toCartAddRequest())
        }.mapHttpFailure {
            on(400, "DIFFERENT_SHOP_ITEM_IN_CART") throws KoinStoreException.DifferentShopItemInCartException()
            on(400, "MENU_SOLD_OUT") throws KoinStoreException.MenuSoldOutException()
            on(400, "REQUIRED_OPTION_GROUP_MISSING") throws KoinStoreException.RequiredOptionGroupMissingException()
            on(400, "MAX_SELECTION_EXCEEDED") throws KoinStoreException.MaxSelectionExceededException()
            on(400, "INVALID_MENU_IN_SHOP") throws KoinStoreException.InvalidMenuInShopException()
            on(400, "SHOP_CLOSED") throws KoinStoreException.ShopClosedException()
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404, "NOT_FOUND_ORDERABLE_SHOP_MENU_PRICE") throws KoinStoreException.MenuPriceNotFoundException()
            on(404, "NOT_FOUND_ORDERABLE_SHOP_MENU_OPTION") throws KoinStoreException.MenuOptionNotFoundException()
        }
    }

    override suspend fun getCartItems(type: String): Result<Cart> {
        return suspendRunCatching {
            storeRemoteDataSource.getCartItems(type).toCart()
        }.mapHttpFailure {
            on(400, "SHOP_NOT_DELIVERABLE") throws KoinStoreException.ShopNotDeliverableException()
            on(400, "SHOP_NOT_TAKEOUT_AVAILABLE") throws KoinStoreException.ShopNotTakeoutAvailableException()
            on(401) throws KoinStoreException.UnauthorizedException()
        }
    }

    override suspend fun validateCartItems(orderType: String): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.validateCartItems(orderType)
        }.mapHttpFailure {
            on(400, "ORDER_AMOUNT_BELOW_MINIMUM") throws KoinStoreException.OrderAmountBelowMinimumException()
            on(400, "SHOP_CLOSED") throws KoinStoreException.ShopClosedException()
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404) throws KoinStoreException.CartNotFoundException()
        }
    }

    override suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary> {
        return suspendRunCatching {
            storeRemoteDataSource.getCartSummary(orderableShopId).toCartSummary()
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
        }
    }

    override suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary> {
        return suspendRunCatching {
            storeRemoteDataSource.getCartPaymentSummary(type).toCartPaymentSummary()
        }.mapHttpFailure {
            on(400, "SHOP_NOT_DELIVERABLE") throws KoinStoreException.ShopNotDeliverableException()
            on(400, "SHOP_NOT_TAKEOUT_AVAILABLE") throws KoinStoreException.ShopNotTakeoutAvailableException()
            on(401) throws KoinStoreException.UnauthorizedException()
        }
    }

    override suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit> {
        return suspendRunCatching {
            storeRemoteDataSource.getCartItemEdit(cartMenuItemId).toCartItemEdit()
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404) throws KoinStoreException.CartItemNotFoundException()
        }
    }

    override suspend fun resetCart(): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.resetCart()
            Unit
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404, "CART_NOT_FOUND") throws KoinStoreException.CartNotFoundException()
        }
    }

    override suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit> {
        return suspendRunCatching {
            storeRemoteDataSource.deleteCartItem(cartMenuItemId)
            Unit
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
            on(404, "CART_MENU_ITEM_NOT_FOUND") throws KoinStoreException.CartItemNotFoundException()
            on(404, "CART_NOT_FOUND") throws KoinStoreException.CartNotFoundException()
        }
    }

    override suspend fun getCartItemsCount(): Result<CartItemsCount> {
        return suspendRunCatching {
            storeRemoteDataSource.getCartItemsCount().toCartItemsCount()
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
        }
    }

    override suspend fun getOrderInProgress(): Result<List<OrderInProgress>> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderInProgress().map { it.toOrderInProgress() }
        }.mapHttpFailure { }
    }

    override suspend fun getStoreCount(): Result<StoreCount> {
        return suspendRunCatching {
            storeRemoteDataSource.getStoreCount().toStoreCount()
        }
    }

    override suspend fun getStoreEventCount(): Result<Int> {
        return suspendRunCatching {
            storeRemoteDataSource.getStoreEventCount().count
        }
    }

    override suspend fun getOrderHistories(
        page: Int?,
        limit: Int?,
        period: String?,
        status: String?,
        type: String?,
        query: String?
    ): Result<OrderHistoryRelated> {
        return suspendRunCatching {
            storeRemoteDataSource.getOrderHistories(page, limit, period, status, type, query).toOrderHistoryRelated()
        }.mapHttpFailure { }
    }
}
