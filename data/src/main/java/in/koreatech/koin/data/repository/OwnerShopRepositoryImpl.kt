package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreWithMenu
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class OwnerShopRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource,
) : OwnerShopRepository {
    override suspend fun getMyShopList(): List<Store> {
        return ownerRemoteDataSource.getMyShopList().map { it.toStore() }
    }

    override suspend fun getOwnerShopInfo(storeId: Int): StoreDetailInfo {
        return ownerRemoteDataSource.getOwnerShopInfo(storeId).toStoreDetailInfo()
    }

    override suspend fun getOwnerShopMenus(storeId: Int): StoreMenu {
        return ownerRemoteDataSource.getOwnerShopMenus(storeId).toStoreMenu()
    }

    override suspend fun getOwnerShopEvents(storeId: Int): ShopEvents {
        return ownerRemoteDataSource.getOwnerShopEvents(storeId).toStoreDetailEvents()
    }
    override suspend fun deleteOwnerShopEvent(storeId: Int, eventId: Int) {
        ownerRemoteDataSource.deleteOwnerShopEvent(storeId, eventId)
    }
}