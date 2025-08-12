package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.auth.StoreAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.mapper.toReportContent
import `in`.koreatech.koin.data.request.store.CartAddRequest
import `in`.koreatech.koin.data.request.store.CartItemRequest
import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.response.store.BenefitCategoryListResponse
import `in`.koreatech.koin.data.response.store.CartItemEditResponse
import `in`.koreatech.koin.data.response.store.CartItemsCountResponse
import `in`.koreatech.koin.data.response.store.CartPaymentSummaryResponse
import `in`.koreatech.koin.data.response.store.CartResponse
import `in`.koreatech.koin.data.response.store.CartSummaryResponse
import `in`.koreatech.koin.data.response.store.OrderableShopSearchRelatedResponse
import `in`.koreatech.koin.data.response.store.ShopDeliveryAvailableResponse
import `in`.koreatech.koin.data.response.store.ShopDetailResponse
import `in`.koreatech.koin.data.response.store.ShopMenuResponse
import `in`.koreatech.koin.data.response.store.ShopMenusGroupResponse
import `in`.koreatech.koin.data.response.store.ShopMenusResponse
import `in`.koreatech.koin.data.response.store.ShopRelatedListResponse
import `in`.koreatech.koin.data.response.store.ShopResponse
import `in`.koreatech.koin.data.response.store.ShopSummaryResponse
import `in`.koreatech.koin.data.response.store.StoreBenefitResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.domain.model.store.StoreReport
import `in`.koreatech.koin.domain.model.store.StoreSorter
import javax.inject.Inject
import retrofit2.Response

class StoreRemoteDataSource @Inject constructor(
    private val storeApi: StoreApi,
    private val storeAuthApi: StoreAuthApi,
    private val userAuthApi: UserAuthApi
) {
    suspend fun getStoreItems(): List<StoreItemResponse> {
        return storeApi.getShopList().shops
    }

    suspend fun getStoreItemsWithSorting(
        storeSorter: StoreSorter?,
        query: String?
    ): List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithSorting(storeSorter.name, query).shops
        } else {
            storeApi.getShopListWithSorting(StoreSorter.NONE.name, query).shops
        }
    }

    suspend fun getStoreItemsWithOneFilter(
        storeSorter: StoreSorter?,
        filter: String,
        query: String?
    ): List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithOneFilter(storeSorter.name, filter, query).shops
        } else {
            storeApi.getShopListWithOneFilter(StoreSorter.NONE.name, filter, query).shops
        }
    }

    suspend fun getStoreItemsWithTwoFilter(
        storeSorter: StoreSorter?,
        query: String?
    ): List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithTwoFilter(storeSorter.name, query = query).shops
        } else {
            storeApi.getShopListWithTwoFilter(StoreSorter.NONE.name, query = query).shops
        }
    }

    suspend fun getStoreEvents(): List<StoreEventItemReponse> {
        return storeApi.getEventShopList().events
    }

    suspend fun getStoreCategories(): List<StoreCategoriesItemResponse> {
        return storeApi.getCategories().shop_categories
    }

    suspend fun getStoreMenu(storeUid: Int): StoreItemWithMenusResponse {
        return storeApi.getStore(storeUid)
    }

    suspend fun getStoreMenuCategory(storeUid: Int): List<StoreMenuCategoryResponse.MenuCategory> {
        return storeApi.getStoreMenuCategory(storeUid).menuCategories
    }

    suspend fun getShopMenus(storeUid: Int): StoreMenuResponse {
        return storeApi.getShopMenus(storeUid)
    }

    suspend fun getShopEvents(storeUid: Int): StoreDetailEventResponse {
        return storeApi.getShopEvents(storeUid)
    }

    suspend fun getStoreReviews(storeUid: Int): StoreReviewResponse {
        return userAuthApi.getShopReviewsWithAuth(storeUid)
    }

    suspend fun writeReview(
        shopId: Int,
        reviewRequest: ReviewRequest
    ) {
        userAuthApi.writeReview(shopId, reviewRequest)
    }

    suspend fun deleteReview(
        reviewId: Int,
        shopId: Int
    ) {
        userAuthApi.deleteReview(reviewId, shopId)
    }

    suspend fun modifyReview(
        reviewId: Int,
        shopId: Int,
        reviewRequest: ReviewRequest
    ) {
        userAuthApi.modifyReview(reviewId, shopId, reviewRequest)
    }

    suspend fun postReviewReports(
        storeUid: Int,
        reviewId: Int,
        reportList: List<StoreReport>
    ) {
        userAuthApi.postStoreReviewReports(
            storeUid,
            reviewId,
            StoreReviewReportsRequest(reportList.toReportContent())
        )
    }

    suspend fun getStoreBenefitShopList(uid: Int): StoreBenefitResponse {
        return storeApi.getBenefitShopList(uid)
    }

    suspend fun getStoreBenefitCategories(): BenefitCategoryListResponse {
        return storeApi.getBenefitCategories()
    }

    suspend fun getShopSearchRelated(query: String): ShopRelatedListResponse {
        return storeApi.getShopSearchRelated(query)
    }

    suspend fun getOrderableShops(
        sorter: String?,
        filter: List<String>?,
        categoryFilter: Int?,
        minimumOrderAmount: Int?
    ): List<ShopResponse> {
        return storeApi.getOrderableShops(sorter, filter, categoryFilter, minimumOrderAmount)
    }

    suspend fun getNearbyShops(
        sorter: String = StoreSorter.NONE.name,
        filter: List<String>? = null,
        query: String? = null
    ): StoreResponse {
        return storeApi.getNearbyShops(sorter, filter, query)
    }

    suspend fun getOrderableShopSummary(shopId: Int): ShopSummaryResponse {
        return storeApi.getOrderableShopSummary(shopId)
    }

    suspend fun getOrderableShopDetail(shopId: Int): ShopDetailResponse {
        return storeApi.getOrderableShopDetail(shopId)
    }

    suspend fun getOrderableShopDelivery(shopId: Int): ShopDeliveryAvailableResponse {
        return storeApi.getOrderableShopDelivery(shopId)
    }

    suspend fun getOrderableShopMenus(shopId: Int): List<ShopMenusResponse> {
        return storeApi.getOrderableShopMenus(shopId)
    }

    suspend fun getOrderableShopMenu(shopId: Int, menuId: Int): ShopMenuResponse {
        return storeApi.getOrderableShopMenu(shopId, menuId)
    }

    suspend fun getOrderableShopMenuGroups(shopId: Int): List<ShopMenusGroupResponse> {
        return storeApi.getOrderableShopMenuGroups(shopId)
    }

    suspend fun getOrderableShopSearchRelated(keyword: String): OrderableShopSearchRelatedResponse {
        return storeApi.getOrderableShopSearchRelated(keyword)
    }

    suspend fun updateCartItem(
        cartMenuItemId: Int,
        cartItemRequest: CartItemRequest
    ) {
        storeAuthApi.updateCartItem(cartMenuItemId, cartItemRequest)
    }

    suspend fun updateCartItemQuantity(
        cartMenuItemId: Int,
        quantity: Int
    ) {
        storeAuthApi.updateCartItemQuantity(cartMenuItemId, quantity)
    }

    suspend fun addCartItem(cartAddRequest: CartAddRequest) {
        storeAuthApi.addCartItem(cartAddRequest)
    }

    suspend fun getCartItems(type: String): CartResponse {
        return storeAuthApi.getCartItems(type)
    }

    suspend fun validateCartItems(orderType: String) {
        storeAuthApi.validateCartItems(orderType)
    }

    suspend fun getCartSummary(orderableShopId: Int): CartSummaryResponse {
        return storeAuthApi.getCartSummary(orderableShopId)
    }

    suspend fun getCartPaymentSummary(type: String): CartPaymentSummaryResponse {
        return storeAuthApi.getCartPaymentSummary(type)
    }

    suspend fun getCartItemEdit(cartMenuItemId: Int): CartItemEditResponse {
        return storeAuthApi.getCartItemEdit(cartMenuItemId)
    }

    suspend fun resetCart(): Response<Unit> {
        return storeAuthApi.resetCart()
    }

    suspend fun deleteCartItem(cartMenuItemId: Int): Response<Unit> {
        return storeAuthApi.deleteCartItem(cartMenuItemId)
    }

    suspend fun getCartItemsCount(): CartItemsCountResponse {
        return storeAuthApi.getCartItemsCount()
    }
}
