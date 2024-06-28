package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OwnerAuthApi {
    @GET(URLConstant.SHOPS.OWNERSHOPS)
    suspend fun getMyShopList(): StoreResponse
    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{id}")
    suspend fun getOwnerShopInfo(@Path("id") uid: Int): StoreItemWithMenusResponse
    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{id}" + "/menus")
    suspend fun getOwnerShopMenus(@Path("id") uid: Int): StoreMenuResponse
    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{id}" + "/events")
    suspend fun getOwnerShopEvents(@Path("id") uid: Int): StoreDetailEventResponse
}