package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    //Get Shop list API
    @GET(URLConstant.SHOPS)
    suspend fun getShopList(): StoreResponse

    //Get Shop list API
    @GET(URLConstant.SHOPS + "/{id}")
    suspend fun getStore(@Path("id") uid: Int): StoreItemWithMenusResponse

    @GET(URLConstant.SHOPS + "/{id}" + "/menus")
    suspend fun getShopMenus(@Path("id") uid: Int): StoreMenuResponse

    @GET(URLConstant.SHOPS + "/{id}" + "/events")
    suspend fun getShopEvents(@Path("id") uid: Int): StoreDetailEventResponse
}