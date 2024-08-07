package `in`.koreatech.koin.data.repository

import android.util.Log
import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.mapper.toCategory
import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreCategories
import `in`.koreatech.koin.data.mapper.toStoreEvent
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreReview
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import retrofit2.HttpException
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    private var stores: List<Store>? = null
    private var storeEvents: List<StoreEvent>? = null
    private var storeCategories: List<StoreCategories>? = null

    override suspend fun getStores(
        storeSorter: StoreSorter?,
        isOperating: Boolean?,
        isDelivery: Boolean?
    ): List<Store> {
        if (stores == null) {
            stores = if(isOperating == true && isDelivery == true){
                storeRemoteDataSource.getStoreItemsWithTwoFilter(storeSorter).map { it.toStore() }
            } else if(isOperating == false && isDelivery == false) {
                storeRemoteDataSource.getStoreItemsWithSorting(storeSorter).map { it.toStore() }
            } else{
                if(isOperating == true) storeRemoteDataSource.getStoreItemsWithOneFilter(storeSorter, "OPEN").map { it.toStore() }
                else storeRemoteDataSource.getStoreItemsWithOneFilter(storeSorter, "DELIVERY").map { it.toStore() }
            }
        }

        return stores!!
    }

    override suspend fun getStoreEvents(): List<StoreEvent> {
        if (storeEvents == null) {
            storeEvents = storeRemoteDataSource.getStoreEvents().map{it.toStoreEvent()}
        }

        return storeEvents!!
    }

    override suspend fun getStoreCategories(): List<StoreCategories> {
        if (storeCategories == null) {
            storeCategories = storeRemoteDataSource.getStoreCategories().map{it.toStoreCategories()}
        }

        return storeCategories!!
    }

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu {
        return storeRemoteDataSource.getStoreMenu(storeId).toStoreWithMenu()
    }

    override suspend fun getStoreMenuCategory(storeId: Int): List<StoreMenuCategory>{
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

    override suspend fun reportReview(
        storeId: Int?,
        reviewId: Int?,
        reportTitle: String,
        reportReason: String
    ): Result<Unit> {
        return try {
            if (storeId != null && reviewId != null) {
                storeRemoteDataSource.postReviewReports(
                    storeId,reviewId, reportTitle, reportReason
                )
            }
            Result.success(Unit)
        }
        catch (e: HttpException) {
            if (e.code() == 204) Result.success(Unit)
            else Result.failure(e)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}