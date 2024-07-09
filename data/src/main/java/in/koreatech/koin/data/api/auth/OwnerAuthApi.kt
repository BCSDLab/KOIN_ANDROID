package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OwnerAuthApi {
    @POST(URLConstant.OWNER.SHOPS)
    suspend fun postMyStore(@Body storeRegisterResponse: StoreRegisterResponse): StoreRegisterResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS)
    suspend fun getMyShopList(): StoreResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{id}")
    suspend fun getOwnerShopInfo(@Path("id") uid: Int): StoreItemWithMenusResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS + "/menus")
    suspend fun getOwnerShopMenus(@Query("shopId") uid: Int): StoreMenuResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{shopId}" + "/event")
    suspend fun getOwnerShopEvents(@Path("shopId") uid: Int): StoreDetailEventResponse
}