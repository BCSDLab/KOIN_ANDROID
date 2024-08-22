package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreCategoriesResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    //Get Shop list API
    @GET(URLConstant.SHOPS.SHOPS)
    suspend fun getShopList(): StoreResponse

    @GET(URLConstant.SHOPS.EVENTS)
    suspend fun getEventShopList(): StoreEventResponse

    @GET(URLConstant.SHOPS.CATERGORIES)
    suspend fun getCategories(): StoreCategoriesResponse

    //Get Shop list API
    @GET(URLConstant.SHOPS.SHOPS + "/{id}")
    suspend fun getStore(@Path("id") uid: Int): StoreItemWithMenusResponse

    @GET(URLConstant.SHOPS.SHOPS + "/{shopId}/menus/categories")
    suspend fun getStoreMenuCategory(@Path("shopId") uid: Int): StoreMenuCategoryResponse

    @GET(URLConstant.SHOPS.SHOPS + "/{id}" + "/menus")
    suspend fun getShopMenus(@Path("id") uid: Int): StoreMenuResponse

    @GET(URLConstant.SHOPS.SHOPS + "/{id}" + "/events")
    suspend fun getShopEvents(@Path("id") uid: Int): StoreDetailEventResponse

}