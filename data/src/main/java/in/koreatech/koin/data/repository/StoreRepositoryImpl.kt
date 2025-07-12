package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCart
import `in`.koreatech.koin.data.mapper.toCartAddRequest
import `in`.koreatech.koin.data.mapper.toCartItemEdit
import `in`.koreatech.koin.data.mapper.toCartItemRequest
import `in`.koreatech.koin.data.mapper.toCartPaymentSummary
import `in`.koreatech.koin.data.mapper.toCartSummary
import `in`.koreatech.koin.data.mapper.toCategory
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
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
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
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject
import retrofit2.HttpException

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    private var stores: List<Store>? = null
    private var storeEvents: List<StoreEvent>? = null
    private var storeCategories: List<StoreCategories>? = null

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
        if (storeCategories == null) {
            storeCategories =
                storeRemoteDataSource.getStoreCategories().map { it.toStoreCategories() }
        }

        return storeCategories!!
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

    override suspend fun getOrderableShops(sorter: String?, filter: String?, minimumOrderAmount: Int?): Result<List<Shop>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShops(sorter, filter, minimumOrderAmount).map { it.toShop() }
        }
    }

    override suspend fun getOrderableShopSummary(shopId: Int): Result<ShopSummary> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopSummary(shopId).toShopSummary()
        }
    }

    override suspend fun getOrderableShopDetail(shopId: Int): Result<ShopDetail> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopDetail(shopId).toShopDetail()
        }
    }

    override suspend fun getOrderableShopDelivery(shopId: Int): Result<ShopDeliveryAvailable> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopDelivery(shopId).toShopDeliveryAvailable()
        }
    }

    override suspend fun getOrderableShopMenus(shopId: Int): Result<List<ShopMenus>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenus(shopId).map { it.toShopMenus() }
        }
    }

    override suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): Result<ShopMenu> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenu(shopId, menuId).toShopMenu()
        }
    }

    override suspend fun getOrderableShopMenuGroups(shopId: Int): Result<List<ShopMenusGroup>> {
        return runCatching {
            storeRemoteDataSource.getOrderableShopMenuGroups(shopId).map { it.toShopMenusGroup() }
        }
    }

    override suspend fun updateCartItem(cartMenuItemId: Int, cartItem: CartItem): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.updateCartItem(cartMenuItemId, cartItem.toCartItemRequest())
        }
    }

    override suspend fun updateCartItemQuantity(cartMenuItemId: Int, quantity: Int): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.updateCartItemQuantity(cartMenuItemId, quantity)
        }
    }

    override suspend fun addCartItem(cartAdd: CartAdd): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.addCartItem(cartAdd.toCartAddRequest())
        }
    }

    override suspend fun getCartItems(): Result<Cart> {
        return runCatching {
            storeRemoteDataSource.getCartItems().toCart()
        }
    }

    override suspend fun validateCartItems(): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.validateCartItems()
        }
    }

    override suspend fun getCartSummary(orderableShopId: Int): Result<CartSummary> {
        return runCatching {
            storeRemoteDataSource.getCartSummary(orderableShopId).toCartSummary()
        }
    }

    override suspend fun getCartPaymentSummary(type: String): Result<CartPaymentSummary> {
        return runCatching {
            storeRemoteDataSource.getCartPaymentSummary(type).toCartPaymentSummary()
        }
    }

    override suspend fun getCartItemEdit(cartMenuItemId: Int): Result<CartItemEdit> {
        return runCatching {
            storeRemoteDataSource.getCartItemEdit(cartMenuItemId).toCartItemEdit()
        }
    }

    override suspend fun resetCart(): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.resetCart()
        }
    }

    override suspend fun deleteCartItem(cartMenuItemId: Int): Result<Unit> {
        return runCatching {
            storeRemoteDataSource.deleteCartItem(cartMenuItemId)
        }
    }
}
