package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.mapper.toReportContent
import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.response.store.BenefitCategoryListResponse
import `in`.koreatech.koin.data.response.store.BenefitCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreBenefitResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.domain.model.store.StoreReport
import `in`.koreatech.koin.domain.model.store.StoreSorter
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val storeApi: StoreApi,
    private val userAuthApi: UserAuthApi,
) {
    suspend fun getStoreItems() : List<StoreItemResponse> {
        return storeApi.getShopList().shops
    }

    suspend fun getStoreItemsWithSorting(storeSorter: StoreSorter?) : List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithSorting(storeSorter.name).shops
        } else{
            storeApi.getShopListWithSorting(StoreSorter.NONE.name).shops
        }
    }

    suspend fun getStoreItemsWithOneFilter(storeSorter: StoreSorter?, filter: String) : List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithOneFilter(storeSorter.name, filter).shops
        } else{
            storeApi.getShopListWithOneFilter(StoreSorter.NONE.name, filter).shops
        }
    }

    suspend fun getStoreItemsWithTwoFilter(storeSorter: StoreSorter?) : List<StoreItemResponse> {
        return if (storeSorter != null) {
            storeApi.getShopListWithTwoFilter(storeSorter.name).shops
        } else{
            storeApi.getShopListWithTwoFilter(StoreSorter.NONE.name).shops
        }
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

    suspend fun modifyReview(reviewId: Int, shopId: Int, reviewRequest: ReviewRequest) {
        userAuthApi.modifyReview(reviewId, shopId, reviewRequest)
    }
    suspend fun postReviewReports(storeUid: Int, reviewId: Int, reportList:List<StoreReport>){
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


}