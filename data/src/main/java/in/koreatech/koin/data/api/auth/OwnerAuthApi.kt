package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.owner.OwnerEventResponse
import `in`.koreatech.koin.data.response.owner.OwnerStoreResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreMenuInfoResponse
import `in`.koreatech.koin.data.response.store.StoreMenuRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
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
    suspend fun postMyStore(
        @Body storeRegisterResponse: StoreRegisterResponse
    )

    @GET(URLConstant.SHOPS.OWNERSHOPS.OWNERSHOPS)
    suspend fun getMyShopList(): OwnerStoreResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS.ID.ID)
    suspend fun getOwnerShopInfo(
        @Path("id") uid: Int
    ): StoreRegisterResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS.MENUS.MENUS)
    suspend fun getOwnerShopMenus(
        @Query("shopId") uid: Int
    ): StoreMenuResponse

    @POST(URLConstant.SHOPS.OWNERSHOPS.ID.MENUS)
    suspend fun postShopMenu(
        @Path("id") storeId: Int,
        @Body storeRegisterResponse: StoreMenuRegisterResponse
    )

    @PUT(URLConstant.SHOPS.OWNERSHOPS.MENUS.MENUID)
    suspend fun putShopModifiedMenu(
        @Path("menuId") menuId: Int,
        @Body storeRegisterResponse: StoreMenuRegisterResponse
    )

    @GET(URLConstant.SHOPS.OWNERSHOPS.MENUS.MENUID)
    suspend fun getMenuInfo(
        @Path("menuId") menuId: Int
    ): StoreMenuInfoResponse

    @GET(URLConstant.SHOPS.OWNERSHOPS.SHOPID.EVENT.EVENT)
    suspend fun getOwnerShopEvents(
        @Path("shopId") uid: Int
    ): StoreDetailEventResponse

    @DELETE(URLConstant.SHOPS.OWNERSHOPS.SHOPID.EVENT.EVENTID)
    suspend fun deleteOwnerShopEvent(
        @Path("shopId") uid: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @PUT(URLConstant.SHOPS.OWNERSHOPS.SHOPID.SHOPID)
    suspend fun modifyOwnerShopInfo(
        @Path("shopId") uid: Int,
        @Body storeInfo: StoreRegisterResponse
    )

    @POST(URLConstant.SHOPS.OWNERSHOPS.ID.EVENT)
    suspend fun postOwnerShopEvent(
        @Path("id") uid: Int,
        @Body storeRegisterResponse: OwnerEventResponse
    )
}
