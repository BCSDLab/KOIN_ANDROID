package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val storeApi: StoreApi
) {
    suspend fun getStoreItems() : List<StoreItemResponse> {
        return storeApi.getShopList().shops
    }

    suspend fun getStoreMenu(storeUid: Int) : StoreItemWithMenusResponse {
        return storeApi.getStore(storeUid)
    }
}