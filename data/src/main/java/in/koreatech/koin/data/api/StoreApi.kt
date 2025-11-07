package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.BenefitCategoryListResponse
import `in`.koreatech.koin.data.response.store.OrderableShopSearchRelatedResponse
import `in`.koreatech.koin.data.response.store.ShopDeliveryAvailableResponse
import `in`.koreatech.koin.data.response.store.ShopDetailResponse
import `in`.koreatech.koin.data.response.store.ShopMenuResponse
import `in`.koreatech.koin.data.response.store.ShopMenusGroupResponse
import `in`.koreatech.koin.data.response.store.ShopMenusResponse
import `in`.koreatech.koin.data.response.store.ShopRelatedListResponse
import `in`.koreatech.koin.data.response.store.ShopResponse
import `in`.koreatech.koin.data.response.store.ShopSearchRelatedResponse
import `in`.koreatech.koin.data.response.store.ShopSummaryResponse
import `in`.koreatech.koin.data.response.store.StoreBenefitResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApi {
    // Get Shop list API
    @GET(URLConstant.SHOPS.SHOPS_V2)
    suspend fun getShopList(): StoreResponse

    @GET(URLConstant.SHOPS.SHOPS_V2)
    suspend fun getShopListWithSorting(
        @Query("sorter") sorter: String,
        @Query("query") query: String?
    ): StoreResponse

    @GET(URLConstant.SHOPS.SHOPS_V2)
    suspend fun getShopListWithOneFilter(
        @Query("sorter") sorter: String,
        @Query("filter") filter: String,
        @Query("query") query: String?
    ): StoreResponse

    @GET(URLConstant.SHOPS.SHOPS_V2)
    suspend fun getShopListWithTwoFilter(
        @Query("sorter") sorter: String,
        @Query("filter") OPEN: String = "OPEN",
        @Query("filter") DELIVERY: String = "DELIVERY",
        @Query("query") query: String?
    ): StoreResponse

    @GET(URLConstant.SHOPS.EVENTS)
    suspend fun getEventShopList(): StoreEventResponse

    @GET(URLConstant.SHOPS.CATERGORIES)
    suspend fun getCategories(): StoreCategoriesResponse

    // Get Shop list API
    @GET(URLConstant.SHOPS.ID.ID)
    suspend fun getStore(
        @Path("id") uid: Int
    ): StoreItemWithMenusResponse

    @GET(URLConstant.SHOPS.SHOPID.MENUS.CATEGORIES)
    suspend fun getStoreMenuCategory(
        @Path("shopId") uid: Int
    ): StoreMenuCategoryResponse

    @GET(URLConstant.SHOPS.ID.MENUS)
    suspend fun getShopMenus(
        @Path("id") uid: Int
    ): StoreMenuResponse

    @GET(URLConstant.SHOPS.ID.EVENTS)
    suspend fun getShopEvents(
        @Path("id") uid: Int
    ): StoreDetailEventResponse

    @GET(URLConstant.SHOPS.ID.REVIEWS)
    suspend fun getShopReviews(
        @Path("id") uid: Int
    ): StoreReviewResponse

    @GET(URLConstant.BENEFIT.SHOPS)
    suspend fun getBenefitShopList(
        @Path("id") uid: Int
    ): StoreBenefitResponse

    @GET(URLConstant.BENEFIT.CATEGORIES)
    suspend fun getBenefitCategories(): BenefitCategoryListResponse

    @GET(URLConstant.SHOPS.QUERY)
    suspend fun getShopSearchRelated(
        @Path("query") query: String
    ): ShopRelatedListResponse

    @GET("/v2/shops/search/related")
    suspend fun getShopSearchRelatedV2(
        @Query("keyword") keyword: String
    ): ShopSearchRelatedResponse

    @GET("/order/shops")
    suspend fun getOrderableShops(
        @Query("sorter") sorter: String?,
        @Query("filter") filter: List<String>?,
        @Query("category_filter") categoryFilter: Int?,
        @Query("minimum_order_amount") minimumOrderAmount: Int?
    ): List<ShopResponse>

    @GET(URLConstant.SHOPS.SHOPS_V3)
    suspend fun getNearbyShops(
        @Query("sorter") sorter: String,
        @Query("filter") filter: List<String>?,
        @Query("query") query: String?
    ): StoreResponse

    @GET("/order/shop/{orderableShopId}/summary")
    suspend fun getOrderableShopSummary(
        @Path("orderableShopId") shopId: Int
    ): ShopSummaryResponse

    @GET("/order/shop/{orderableShopId}/detail")
    suspend fun getOrderableShopDetail(
        @Path("orderableShopId") shopId: Int
    ): ShopDetailResponse

    @GET("/order/shop/{orderableShopId}/delivery")
    suspend fun getOrderableShopDelivery(
        @Path("orderableShopId") shopId: Int
    ): ShopDeliveryAvailableResponse

    @GET("/order/shop/{orderableShopId}/menus")
    suspend fun getOrderableShopMenus(
        @Path("orderableShopId") shopId: Int
    ): List<ShopMenusResponse>

    @GET("/order/shop/{orderableShopId}/menus/{orderableShopMenuId}")
    suspend fun getOrderableShopMenu(
        @Path("orderableShopId") shopId: Int,
        @Path("orderableShopMenuId") menuId: Int
    ): ShopMenuResponse

    @GET("/order/shop/{orderableShopId}/menus/groups")
    suspend fun getOrderableShopMenuGroups(
        @Path("orderableShopId") shopId: Int
    ): List<ShopMenusGroupResponse>

    @GET("/order/shop/search/related")
    suspend fun getOrderableShopSearchRelated(
        @Query("keyword") keyword: String
    ): OrderableShopSearchRelatedResponse
}
