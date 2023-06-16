package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    private var stores: List<Store>? = null

    override suspend fun getStores(): List<Store> {
        if (stores == null) {
            stores = storeRemoteDataSource.getStoreItems().map { it.toStore() }
        }

        return stores!!
    }

    override suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu {
        return storeRemoteDataSource.getStoreMenu(storeId).toStoreWithMenu()
    }

    override suspend fun invalidateStores() {
        stores = null
    }
}