package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
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
        isDelivery: Boolean? = null
    ): List<Store>
    suspend fun getStoreEvents(): List<StoreEvent>
    suspend fun getStoreCategories(): List<StoreCategories>
    suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu

    suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory>

    suspend fun getShopMenus(storeId: Int): StoreMenu
    suspend fun getShopEvents(storeId: Int): ShopEvents
    suspend fun getStoreReviews(storeId: Int): StoreReview
    suspend fun invalidateStores()
    suspend fun writeReview(shopId: Int, content: Review)
    suspend fun deleteReview(reviewId: Int, shopId: Int)
    suspend fun modifyReview(reviewId: Int, shopId: Int, content: Review)

    suspend fun reportReview(
        storeId: Int?,
        reviewId: Int?,
        reportList: List<StoreReport>?
    ): Result<Unit>
}