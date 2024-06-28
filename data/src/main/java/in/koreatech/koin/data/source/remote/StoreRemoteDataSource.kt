package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val storeApi: StoreApi
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
    suspend fun getMyShopList() : List<StoreItemResponse> {
        return storeApi.getMyShopList().shops
    }
    suspend fun getOwnerShopInfo(storeUid: Int) : StoreItemWithMenusResponse {
        return storeApi.getOwnerShopInfo(storeUid)
    }
    suspend fun getShopMenus(storeUid: Int): StoreMenuResponse {
        return storeApi.getShopMenus(storeUid)
    }
    suspend fun getOwnerShopMenus(storeUid: Int): StoreMenuResponse {
        return storeApi.getOwnerShopMenus(storeUid)
    }
    suspend fun getShopEvents(storeUid: Int): StoreDetailEventResponse {
        return storeApi.getShopEvents(storeUid)
    }
    suspend fun getOwnerShopEvents(storeUid: Int): StoreDetailEventResponse {
        return storeApi.getOwnerShopEvents(storeUid)
    }
}