package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCart
import `in`.koreatech.koin.data.mapper.toCartAddRequest
import `in`.koreatech.koin.data.mapper.toCartItemEdit
import `in`.koreatech.koin.data.mapper.toCartItemRequest
import `in`.koreatech.koin.data.mapper.toCartItemsCount
import `in`.koreatech.koin.data.mapper.toCartPaymentSummary
import `in`.koreatech.koin.data.mapper.toCartSummary
import `in`.koreatech.koin.data.mapper.toCategory
import `in`.koreatech.koin.data.mapper.toOrderableShopSearchRelated
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
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreEvent
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreReview
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.source.local.StoreLocalDataSource
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
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
import `in`.koreatech.koin.domain.model.store.OrderableShopSearchRelated
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
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject
import retrofit2.HttpException

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
    private val storeLocalDataSource: StoreLocalDataSource
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
            stores = storeRemoteDataSource.getStoreItems().map { it.toStore() }
        }

        return if (isOperating == true && isDelivery == true) {
            stores!!.filter { it.isOpen && it.isDeliveryOk }
        } else if (isOperating == false && isDelivery == false) {
            stores!!.filter { !it.isOpen && !it.isDeliveryOk }
        } else {
            if (isOperating == true) {
                stores!!.filter { it.isOpen }
            } else {
                stores!!.filter { it.isDeliveryOk }
            }
        }
    }

    override suspend fun getStoreEvents(): List<StoreEvent> {
        if (storeEvents == null) {
            storeEvents = storeRemoteDataSource.getStoreEvents().map { it.toStoreEvent() }
        }

        return storeEvents!!
    }

    override suspend fun getStoreCategories(): List<StoreCategories> {
        return storeLocalDataSource.getCachedStoreCategories().let { cachedCategories ->
            cachedCategories?.map { it.toStoreCategories() } ?: run {
                storeRemoteDataSource.getStoreCategories().also {
                    storeLocalDataSource.setCachedStoreCategories(it)
                }.map { it.toStoreCategories() }
            }
        }
    }

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu {
        return storeRemoteDataSource.getStoreMenu(storeId).toStoreWithMenu()
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
    ) {
        storeRemoteDataSource.deleteReview(reviewId, shopId)
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

    override suspend fun getOrderableShops(
        sorter: String?,
        filter: List<String>,
        categoryFilter: Int?,
        minimumOrderAmount: Int?
    ): Result<List<Shop>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShops(sorter, filter, categoryFilter, minimumOrderAmount).map { it.toShop() }
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getNearbyShops(): Result<List<Shop>> {
        return runCatching {
            storeLocalDataSource.getCachedNearbyShops()?.map { it.toShop() } ?: storeRemoteDataSource.getNearbyShops().shops.also {
                storeLocalDataSource.setCachedNearbyShops(it)
            }.map {
                it.toShop()
            }
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopSummary(shopId).toShopSummary()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopDetail(shopId).toShopDetail()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopDelivery(shopId).toShopDeliveryAvailable()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenus(shopId).map { it.toShopMenus() }
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): Result<ShopMenu> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenu(shopId, menuId).toShopMenu()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.MenuNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenuGroups(shopId).map { it.toShopMenusGroup() }
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> KoinStoreException.ShopNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getOrderableShopSearchRelated(query: String): Result<OrderableShopSearchRelated> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopSearchRelated(query).toOrderableShopSearchRelated()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        e.getErrorResponse().toKoinUnknownErrorException()
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun updateCartItem(cartMenuItemId: Int, cartItem: CartItem): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.updateCartItem(cartMenuItemId, cartItem.toCartItemRequest())
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> when (e.getErrorResponse().code) {
                                "REQUIRED_OPTION_GROUP_MISSING" -> KoinStoreException.RequiredOptionGroupMissingException()
                                "MIN_SELECTION_NOT_MET" -> KoinStoreException.MinimumSelectionNotMetException()
                                "MAX_SELECTION_EXCEEDED" -> KoinStoreException.MaxSelectionExceededException()
                                "INVALID_OPTION_IN_GROUP" -> KoinStoreException.InvalidOptionInGroupException()
                                else -> KoinStoreException.BadRequestException()
                            }

                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> when (e.getErrorResponse().code) {
                                "CART_MENU_ITEM_NOT_FOUND" -> KoinStoreException.CartItemNotFoundException()
                                "MENU_OPTION_NOT_FOUND" -> KoinStoreException.MenuOptionNotFoundException()
                                "MENU_PRICE_NOT_FOUND" -> KoinStoreException.MenuPriceNotFoundException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun updateCartItemQuantity(cartMenuItemId: Int, quantity: Int): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.updateCartItemQuantity(cartMenuItemId, quantity)
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> KoinStoreException.InvalidQuantityException()
                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> when (e.getErrorResponse().code) {
                                "CART_MENU_ITEM_NOT_FOUND" -> KoinStoreException.CartItemNotFoundException()
                                "CART_NOT_FOUND" -> KoinStoreException.CartNotFoundException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun addCartItem(cartAdd: CartAdd): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.addCartItem(cartAdd.toCartAddRequest())
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> when (e.getErrorResponse().code) {
                                "DIFFERENT_SHOP_ITEM_IN_CART" -> KoinStoreException.DifferentShopItemInCartException()
                                "MENU_SOLD_OUT" -> KoinStoreException.MenuSoldOutException()
                                "REQUIRED_OPTION_GROUP_MISSING" -> KoinStoreException.RequiredOptionGroupMissingException()
                                "MAX_SELECTION_EXCEEDED" -> KoinStoreException.MaxSelectionExceededException()
                                "INVALID_MENU_IN_SHOP" -> KoinStoreException.InvalidMenuInShopException()
                                "SHOP_CLOSED" -> KoinStoreException.ShopClosedException()
                                else -> KoinStoreException.BadRequestException()
                            }

                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> when (e.getErrorResponse().code) {
                                "NOT_FOUND_ORDERABLE_SHOP_MENU_PRICE" -> KoinStoreException.MenuPriceNotFoundException()
                                "NOT_FOUND_ORDERABLE_SHOP_MENU_OPTION" -> KoinStoreException.MenuOptionNotFoundException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getCartItems(type: String): Result<Cart> {
        return runCatching {
            storeRemoteDataSource.getCartItems(type).toCart()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> when (e.getErrorResponse().code) {
                                "SHOP_NOT_DELIVERABLE" -> KoinStoreException.ShopNotDeliverableException()
                                "SHOP_NOT_TAKEOUT_AVAILABLE" -> KoinStoreException.ShopNotTakeoutAvailableException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            401 -> KoinStoreException.UnauthorizedException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun validateCartItems(): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.validateCartItems()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> when (e.getErrorResponse().code) {
                                "ORDER_AMOUNT_BELOW_MINIMUM" -> KoinStoreException.OrderAmountBelowMinimumException()
                                "SHOP_CLOSED" -> KoinStoreException.ShopClosedException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> KoinStoreException.CartNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary> {
        return runCatching {
            storeRemoteDataSource.getCartSummary(orderableShopId).toCartSummary()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> KoinStoreException.UnauthorizedException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary> {
        return runCatching {
            storeRemoteDataSource.getCartPaymentSummary(type).toCartPaymentSummary()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> when (e.getErrorResponse().code) {
                                "SHOP_NOT_DELIVERABLE" -> KoinStoreException.ShopNotDeliverableException()
                                "SHOP_NOT_TAKEOUT_AVAILABLE" -> KoinStoreException.ShopNotTakeoutAvailableException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            401 -> KoinStoreException.UnauthorizedException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit> {
        return runCatching {
            storeRemoteDataSource.getCartItemEdit(cartMenuItemId).toCartItemEdit()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> KoinStoreException.CartItemNotFoundException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun resetCart(): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.resetCart()
            Unit
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> when (e.getErrorResponse().code) {
                                "CART_NOT_FOUND" -> KoinStoreException.CartNotFoundException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.deleteCartItem(cartMenuItemId)
            Unit
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> KoinStoreException.UnauthorizedException()
                            404 -> when (e.getErrorResponse().code) {
                                "CART_MENU_ITEM_NOT_FOUND" -> KoinStoreException.CartNotFoundException()
                                "CART_NOT_FOUND" -> KoinStoreException.CartNotFoundException()
                                else -> e.getErrorResponse().toKoinUnknownErrorException()
                            }

                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }

    override suspend fun getCartItemsCount(): Result<CartItemsCount> {
        return runCatching {
            storeRemoteDataSource.getCartItemsCount().toCartItemsCount()
        }.onFailure { e ->
            return Result.failure(
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> KoinStoreException.UnauthorizedException()
                            else -> e.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> e
                }
            )
        }
    }
}
