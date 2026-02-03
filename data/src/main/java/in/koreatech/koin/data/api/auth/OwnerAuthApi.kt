package `in`.koreatech.koin.data.api.auth

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
    @POST("owner/shops")
    suspend fun postMyStore(
        @Body storeRegisterResponse: StoreRegisterResponse
    )

    @GET("owner/shops")
    suspend fun getMyShopList(): OwnerStoreResponse

    @GET("owner/shops/{id}")
    suspend fun getOwnerShopInfo(
        @Path("id") uid: Int
    ): StoreRegisterResponse

    @GET("owner/shops/menus")
    suspend fun getOwnerShopMenus(
        @Query("shopId") uid: Int
    ): StoreMenuResponse

    @POST("owner/shops/{id}/menus")
    suspend fun postShopMenu(
        @Path("id") storeId: Int,
        @Body storeRegisterResponse: StoreMenuRegisterResponse
    )

    @PUT("owner/shops/menus/{menuId}")
    suspend fun putShopModifiedMenu(
        @Path("menuId") menuId: Int,
        @Body storeRegisterResponse: StoreMenuRegisterResponse
    )

    @GET("owner/shops/menus/{menuId}")
    suspend fun getMenuInfo(
        @Path("menuId") menuId: Int
    ): StoreMenuInfoResponse

    @GET("owner/shops/{shopId}/event")
    suspend fun getOwnerShopEvents(
        @Path("shopId") uid: Int
    ): StoreDetailEventResponse

    @DELETE("owner/shops/{shopId}/events/{eventId}")
    suspend fun deleteOwnerShopEvent(
        @Path("shopId") uid: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @PUT("owner/shops/{shopId}")
    suspend fun modifyOwnerShopInfo(
        @Path("shopId") uid: Int,
        @Body storeInfo: StoreRegisterResponse
    )

    @POST("owner/shops/{id}/event")
    suspend fun postOwnerShopEvent(
        @Path("id") uid: Int,
        @Body storeRegisterResponse: OwnerEventResponse
    )
}
