package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val storeApi: StoreApi,
    private val userAuthApi: UserAuthApi,
) {
    suspend fun getStoreItems() : List<StoreItemResponse> {
        return storeApi.getShopList().shops
    }

    suspend fun getStoreEvents(): List<StoreEventItemReponse>{
        return storeApi.getEventShopList().events
    }

    suspend fun getStoreCategories(): List<StoreCategoriesItemResponse>{
        return storeApi.getCategories().shop_categories
    }

    suspend fun getStoreMenu(storeUid: Int) : StoreItemWithMenusResponse {
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

    suspend fun writeReview(shopId: Int, reviewRequest: ReviewRequest) {
        userAuthApi.writeReview(shopId, reviewRequest)
    }

    suspend fun deleteReview(reviewId: Int, shopId:Int) {
        userAuthApi.deleteReview(reviewId, shopId)
    }
}