package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toMyStoreDayOffResponse
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toStore
import `in`.koreatech.koin.data.mapper.toStoreDetailEvents
import `in`.koreatech.koin.data.mapper.toStoreDetailInfo
import `in`.koreatech.koin.data.mapper.toStoreMenu
import `in`.koreatech.koin.data.mapper.toStoreMenuInfo
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuInfo
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
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

    override suspend fun getStoreMenuInfo(menuId: Int): StoreMenuInfo {
        return ownerRemoteDataSource.getStoreMenuInfo(menuId).toStoreMenuInfo()
    }

    override suspend fun deleteOwnerShopEvent(storeId: Int, eventId: Int) {
        ownerRemoteDataSource.deleteOwnerShopEvent(storeId, eventId)
    }

    override suspend fun modifyOwnerShopInfo(
        shopId: Int,
        storeDetailInfo: StoreDetailInfo,
    ) {
        ownerRemoteDataSource.modifyOwnerShopInfo(
            shopId, StoreRegisterResponse(
                address = storeDetailInfo.address ?: "",
                categoryIds = storeDetailInfo.categoryIds.toMutableList().apply { add(1) },
                delivery = storeDetailInfo.isDeliveryOk,
                delivery_price = storeDetailInfo.deliveryPrice,
                description = storeDetailInfo.description,
                imageUrls = storeDetailInfo.imageUrls ?: emptyList(),
                name = storeDetailInfo.name,
                open = storeDetailInfo.operatingTime.toMyStoreDayOffResponse(),
                payBank = storeDetailInfo.isBankOk,
                payCard = storeDetailInfo.isCardOk,
                phone = storeDetailInfo.phone.toPhoneNumber()
            )
        )
    }
}