package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuInfoResponse
import `in`.koreatech.koin.data.response.store.StoreMenuRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OwnerAuthApi {
    @POST(URLConstant.OWNER.SHOPS)
    suspend fun postMyStore(@Body storeRegisterResponse: StoreRegisterResponse): StoreRegisterResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS)
    suspend fun getMyShopList(): StoreResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{id}")
    suspend fun getOwnerShopInfo(@Path("id") uid: Int): StoreRegisterResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS + "/menus")
    suspend fun getOwnerShopMenus(@Query("shopId") uid: Int): StoreMenuResponse

    @POST(URLConstant.SHOPS.OWNERSHOPS + "/{id}"+"/menus")
    suspend fun postShopMenu(@Path("id") storeId: Int, @Body storeRegisterResponse: StoreMenuRegisterResponse): StoreMenuRegisterResponse

    @PUT(URLConstant.SHOPS.OWNERSHOPS +"/menus"+ "/{menuId}")
    suspend fun putShopModifiedMenu(@Path("menuId") menuId: Int, @Body storeRegisterResponse: StoreMenuRegisterResponse)
    @GET(URLConstant.SHOPS.OWNERSHOPS +"/menus" + "/{menuId}")
    suspend fun getMenuInfo(@Path("menuId") menuId: Int): StoreMenuInfoResponse
    @GET(URLConstant.SHOPS.OWNERSHOPS + "/{shopId}" + "/event")
    suspend fun getOwnerShopEvents(@Path("shopId") uid: Int): StoreDetailEventResponse

    @DELETE(URLConstant.SHOPS.OWNERSHOPS + "/{shopId}" + "/events" + "/{eventId}")
    suspend fun deleteOwnerShopEvent(
        @Path("shopId") uid: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @PUT(URLConstant.SHOPS.OWNERSHOPS + "/{shopId}")
    suspend fun modifyOwnerShopInfo(
        @Path("shopId") uid: Int,
        @Body storeInfo: StoreRegisterResponse
    )
}