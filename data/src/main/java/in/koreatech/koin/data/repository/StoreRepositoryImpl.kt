package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreCategories
import `in`.koreatech.koin.data.mapper.toStoreEvent
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    private var stores: List<Store>? = null
    private var storeEvents: List<StoreEvent>? = null
    private var storeCategories: List<StoreCategories>? = null

    override suspend fun getStores(): List<Store> {
        if (stores == null) {
            stores = storeRemoteDataSource.getStoreItems().map { it.toStore() }
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

    override suspend fun getShopMenus(storeId: Int): StoreMenu {
        return storeRemoteDataSource.getShopMenus(storeId).toStoreMenu()
    }

    override suspend fun getShopEvents(storeId: Int): ShopEvents {
        return storeRemoteDataSource.getShopEvents(storeId).toStoreDetailEvents()
    }

    override suspend fun invalidateStores() {
        stores = null
    }
}