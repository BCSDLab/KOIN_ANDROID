package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.response.store.BenefitCategoryListResponse
import `in`.koreatech.koin.data.response.store.ShopMenuOptionsResponse
import `in`.koreatech.koin.data.response.store.ShopMenusResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreDayOffResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoriesResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoryResponse
import `in`.koreatech.koin.data.response.store.StoreMenuInfoResponse
import `in`.koreatech.koin.data.response.store.StoreMenuRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreReviewContentResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.data.response.store.StoreReviewStatisticsResponse
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuInfo
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.model.store.BenefitCategory
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreReport
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import `in`.koreatech.koin.domain.model.store.StoreReviewStatistics
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.domain.util.ext.localDayOfWeekName

fun StoreItemResponse.toStore(): Store = Store(
    uid = uid ?: 0,
    name = name ?: "",
    phone = phone ?: "",
    isDeliveryOk = isDeliveryOk ?: false,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    isEvent = isEvent ?: false,
    isOpen = isOpen ?: false,
    averageRate = averageRate ?: 0.0,
    reviewCount = reviewCount ?: 0,
    open = open?.filter { it.dayOfWeek == localDayOfWeekName }?.map {
        Store.OpenData(
            dayOfWeek = it.dayOfWeek ?: "",
            closed = it.closed ?: false,
            openTime = it.openTime ?: "",
            closeTime = it.closeTime ?: ""
        )
    }.orEmpty().getOrElse(0) { Store.OpenData(localDayOfWeekName, false, "00:00", "00:00") },
    categoryIds = categoryIds?.map { it.toStoreCategory() }.orEmpty()
)

fun StoreEventItemReponse.toStoreEvent(): StoreEvent = StoreEvent(
    shopId = shopId,
    shopName = shopName ?: "",
    eventId = eventId,
    title = title ?: "",
    content = content ?: "",
    thumbnailImages = thumbnailImages ?: ArrayList<String>(),
    startDate = startDate ?: "",
    endDate = endDate ?: ""
)

fun StoreCategoriesItemResponse.toStoreCategories(): StoreCategories = StoreCategories(
    id = id,
    imageUrl = imageUrl,
    name = name
)

fun StoreItemWithMenusResponse.toStoreWithMenu(): StoreWithMenu = StoreWithMenu(
    uid = uid,
    name = name,
    phone = phone ?: "",
    address = address ?: "",
    description = description?.replace("\\n", System.getProperty("line.separator") ?: "\n"),
    isDeliveryOk = isDeliveryOk ?: false,
    deliveryPrice = deliveryPrice ?: 0,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    updateAt = updateAt,
    isEvent = isEvent ?: false,
    open = open?.filter { it.dayOfWeek == localDayOfWeekName }?.map {
        Store.OpenData(
            dayOfWeek = it.dayOfWeek ?: "",
            closed = it.closed ?: false,
            openTime = it.openTime ?: "",
            closeTime = it.closeTime ?: ""
        )
    }.orEmpty().getOrElse(0) { Store.OpenData(localDayOfWeekName, false, "00:00", "00,00") },
    imageUrls = imageUrls ?: emptyList(),
    shopCategories = shopCategories?.map { it.toCategory() }.orEmpty(),
    menuCategories = menuCategories?.map { it.toCategory() }.orEmpty(),
    bank = bank ?: null,
    accountNumber = accountNumber ?: null
)

fun List<StoreMenuCategoryResponse.MenuCategory>.toCategory(): List<StoreMenuCategory> {
    val responseList = ArrayList<StoreMenuCategory>()
    for (category in this) {
        responseList.add(StoreMenuCategory(category.id, category.name))
    }
    return responseList
}

fun StoreItemWithMenusResponse.CategoriesResponseDTO.toCategory() = StoreWithMenu.Category(
    id = id,
    name = name
)

fun StoreMenuResponse.toStoreMenu() = StoreMenu(
    menuCategories = menuCategories?.map { it.toStoreMenuCategories() }.orEmpty()
)

fun StoreMenuCategoriesResponse.toStoreMenuCategories() = StoreMenuCategories(
    id = id,
    name = name,
    menus = menus?.map { it.toShopMenus() }.orEmpty()
)

fun ShopMenusResponse.toShopMenus() = ShopMenus(
    id = id,
    name = name,
    isHidden = isHidden,
    isSingle = isSingle,
    singlePrice = singlePrice,
    optionPrices = optionPrices?.map { it.toShopMenuOptions() }.orEmpty(),
    description = description,
    imageUrls = imageUrls.orEmpty()
)

fun ShopMenuOptionsResponse.toShopMenuOptions() = ShopMenus.ShopMenuOptions(
    option = option ?: "",
    price = price
)

fun StoreDetailEventResponse.toStoreDetailEvents(): ShopEvents = ShopEvents(
    events = events?.map { it.toStoreDetailEvent() }.orEmpty()
)

fun StoreRegisterResponse.toStoreDetailInfo(): StoreDetailInfo = StoreDetailInfo(
    address = address ?: "",
    categoryIds = categoryIds ?: emptyList(),
    deliveryPrice = delivery_price ?: 0,
    description = description ?: "",
    imageUrls = imageUrls ?: emptyList(),
    isBankOk = payBank ?: false,
    isCardOk = payCard ?: false,
    isDeliveryOk = delivery ?: false,
    name = name ?: "",
    operatingTime = open?.toOperatingTime() ?: emptyList(),
    phone = phone ?: "",
    bank = null,
    accountNumber = null,
)

fun StoreDetailEventResponse.StoreEventDTO.toStoreDetailEvent() = ShopEvent(
    shopId = shopId ?: 0,
    shopName = shopName ?: "",
    eventId = eventId ?: 0,
    title = title ?: "",
    content = content ?: "",
    thumbnailImages = thumbnailImages ?: emptyList(),
    startDate = startDate ?: "",
    endDate = endDate ?: ""
)

fun StoreReviewResponse.toStoreReview() = StoreReview(
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage,
    statistics = statistics.toStoreReviewStatistics(),
    reviews = reviews.toStoreReviewContentList()
)

fun List<OperatingTime>.toMyStoreDayOffResponse(): ArrayList<StoreDayOffResponse> {
    val responseList = ArrayList<StoreDayOffResponse>()
    for (dayOff in this) {
        val response =
            StoreDayOffResponse(dayOff.closeTime, dayOff.closed, dayOff.dayOfWeek, dayOff.openTime)
        responseList.add(response)
    }
    return responseList
}


fun String.toStringArray(): ArrayList<String> {
    val responseList = ArrayList<String>()
    responseList.add(this)
    return responseList
}

fun Int.toCategory(): List<Int> {
    val responseList = ArrayList<Int>()

    responseList.add(1)
    responseList.add(this)

    return responseList
}

fun List<StoreDayOffResponse>.toOperatingTime(): List<OperatingTime> {
    val responseList = ArrayList<OperatingTime>()
    for (dayOff in this) {
        val response = OperatingTime(
            dayOff.closeTime ?: "",
            dayOff.closed,
            dayOff.dayOfWeek,
            dayOff.openTime ?: "",
        )
        responseList.add(response)
    }
    return responseList
}

fun List<StoreMenuOptionPrice>.toOptionPriceList(): List<StoreMenuRegisterResponse.OptionPrice> {
    val responseList = ArrayList<StoreMenuRegisterResponse.OptionPrice>()
    for (option in this) {
        val response = StoreMenuRegisterResponse.OptionPrice(option.option, option.price.toInt())

        responseList.add(response)
    }
    return responseList
}

fun StoreMenuInfoResponse.toStoreMenuInfo(): StoreMenuInfo {
    val responseList = ArrayList<StoreMenuOptionPrice>()

    if (this.optionPrices != null) {
        for (priceOption in this.optionPrices) {
            val response = StoreMenuOptionPrice(
                option = priceOption.option,
                price = priceOption.price.toString()
            )

            responseList.add(response)
        }
    }
    return StoreMenuInfo(
        shopId = shopId,
        name = name,
        isSingle = isSingle,
        singlePrice = singlePrice,
        optionPrice = responseList,
        description = description,
        categoryIds = categoryIds,
        imageUrl = imageUrls
    )
}

fun StoreReviewStatisticsResponse.toStoreReviewStatistics() = StoreReviewStatistics(
    averageRating = averageRating,
    ratings = ratings
)

fun List<StoreReviewContentResponse>.toStoreReviewContentList(): List<StoreReviewContent> =
    this.map { response ->
        StoreReviewContent(
            reviewId = response.reviewId ?: 0,
            rating = response.rating ?: 0,
            nickName = response.nickName ?: "",
            content = response.content ?: "",
            imageUrls = response.imageUrls ?: emptyList(),
            menuNames = response.menuNames ?: emptyList(),
            isMine = response.isMine ?: false,
            isModified = response.isModified ?: false,
            isReported = response.isReported ?: false,
            createdAt = response.createdAt ?: ""
        )
    }

fun List<StoreReport>.toReportContent(): List<StoreReviewReportsRequest.ReportContent> {
    val responseList = ArrayList<StoreReviewReportsRequest.ReportContent>()
    for (report in this) {
        val response = StoreReviewReportsRequest.ReportContent(report.title, report.content)
        responseList.add(response)
    }
    return responseList
}


fun BenefitCategoryListResponse.toStoreBenefitCategory(): BenefitCategoryList =
    BenefitCategoryList(this.benefitCategories.map {
        BenefitCategory(
            id = it.id,
            title = it.title?: "",
            detail = it.detail?: "",
            onImageUrl = it.onImageUrl?: "",
            offImageUrl = it.offImageUrl?: ""
        )
    })


