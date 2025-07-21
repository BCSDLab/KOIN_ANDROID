package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.store.CartAddRequest
import `in`.koreatech.koin.data.request.store.CartItemRequest
import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.response.owner.OwnerGetStoreResponse
import `in`.koreatech.koin.data.response.store.BenefitCategoryListResponse
import `in`.koreatech.koin.data.response.store.CartItemEditResponse
import `in`.koreatech.koin.data.response.store.CartPaymentSummaryResponse
import `in`.koreatech.koin.data.response.store.CartResponse
import `in`.koreatech.koin.data.response.store.CartSummaryResponse
import `in`.koreatech.koin.data.response.store.LegacyShopMenusResponse
import `in`.koreatech.koin.data.response.store.ShopDeliveryAvailableResponse
import `in`.koreatech.koin.data.response.store.ShopDetailResponse
import `in`.koreatech.koin.data.response.store.ShopMenuOptionsResponse
import `in`.koreatech.koin.data.response.store.ShopMenuResponse
import `in`.koreatech.koin.data.response.store.ShopMenusGroupResponse
import `in`.koreatech.koin.data.response.store.ShopMenusResponse
import `in`.koreatech.koin.data.response.store.ShopRelatedListResponse
import `in`.koreatech.koin.data.response.store.ShopResponse
import `in`.koreatech.koin.data.response.store.ShopSummaryResponse
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
import `in`.koreatech.koin.domain.model.owner.OwnerGetStore
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuInfo
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.model.store.BenefitCategory
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.CartPaymentSummary
import `in`.koreatech.koin.domain.model.store.CartSummary
import `in`.koreatech.koin.domain.model.store.LegacyShopMenus
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.ShopDeliveryAvailable
import `in`.koreatech.koin.domain.model.store.ShopDetail
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.ShopMenu
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.ShopMenusGroup
import `in`.koreatech.koin.domain.model.store.ShopSearchRelated
import `in`.koreatech.koin.domain.model.store.ShopSearchRelatedList
import `in`.koreatech.koin.domain.model.store.ShopSummary
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
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.ext.localDayOfWeekName

fun StoreItemResponse.toStore(): Store =
    Store(
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
        open =
        open?.filter { it.dayOfWeek == localDayOfWeekName }?.map {
            Store.OpenData(
                dayOfWeek = it.dayOfWeek ?: "",
                closed = it.closed ?: false,
                openTime = it.openTime ?: "",
                closeTime = it.closeTime ?: ""
            )
        }.orEmpty().getOrElse(0) { Store.OpenData(localDayOfWeekName, false, "00:00", "00:00") },
        categoryIds = categoryIds,
        benefitDetails = benefitDetails ?: benefitDetail?.toStringArray() ?: emptyList()
    )

fun StoreEventItemReponse.toStoreEvent(): StoreEvent =
    StoreEvent(
        shopId = shopId,
        shopName = shopName ?: "",
        eventId = eventId,
        title = title ?: "",
        content = content ?: "",
        thumbnailImages = thumbnailImages ?: ArrayList<String>(),
        startDate = startDate ?: "",
        endDate = endDate ?: ""
    )

fun StoreCategoriesItemResponse.toStoreCategories(): StoreCategories =
    StoreCategories(
        id = id,
        imageUrl = imageUrl,
        name = name
    )

fun StoreItemWithMenusResponse.toStoreWithMenu(): StoreWithMenu =
    StoreWithMenu(
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
        open =
        open?.filter { it.dayOfWeek == localDayOfWeekName }?.map {
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

fun StoreItemWithMenusResponse.CategoriesResponseDTO.toCategory() =
    StoreWithMenu.Category(
        id = id,
        name = name
    )

fun StoreMenuResponse.toStoreMenu() =
    StoreMenu(
        menuCategories = menuCategories?.map { it.toStoreMenuCategories() }.orEmpty()
    )

fun StoreMenuCategoriesResponse.toStoreMenuCategories() =
    StoreMenuCategories(
        id = id,
        name = name,
        menus = menus?.map { it.toShopMenus() }.orEmpty()
    )

fun LegacyShopMenusResponse.toShopMenus() =
    LegacyShopMenus(
        id = id,
        name = name,
        isHidden = isHidden,
        isSingle = isSingle,
        singlePrice = singlePrice,
        optionPrices = optionPrices?.map { it.toShopMenuOptions() }.orEmpty(),
        description = description,
        imageUrls = imageUrls.orEmpty()
    )

fun ShopMenuOptionsResponse.toShopMenuOptions() =
    LegacyShopMenus.ShopMenuOptions(
        option = option ?: "",
        price = price
    )

fun StoreDetailEventResponse.toStoreDetailEvents(): ShopEvents =
    ShopEvents(
        events = events?.map { it.toStoreDetailEvent() }.orEmpty()
    )

fun StoreRegisterResponse.toStoreDetailInfo(): StoreDetailInfo =
    StoreDetailInfo(
        address = address ?: "",
        mainCategoryId = mainCategoryId,
        categoryIds = categoryIds ?: emptyList(),
        deliveryPrice = deliveryPrice ?: 0,
        description = description ?: "",
        imageUrls = imageUrls ?: emptyList(),
        isBankOk = payBank ?: false,
        isCardOk = payCard ?: false,
        isDeliveryOk = delivery ?: false,
        name = name ?: "",
        operatingTime = open?.toOperatingTime() ?: emptyList(),
        phone = phone ?: "",
        bank = null,
        accountNumber = null
    )

fun StoreDetailEventResponse.StoreEventDTO.toStoreDetailEvent() =
    ShopEvent(
        shopId = shopId ?: 0,
        shopName = shopName ?: "",
        eventId = eventId ?: 0,
        title = title ?: "",
        content = content ?: "",
        thumbnailImages = thumbnailImages ?: emptyList(),
        startDate = startDate ?: "",
        endDate = endDate ?: ""
    )

fun StoreReviewResponse.toStoreReview() =
    StoreReview(
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
        val response =
            OperatingTime(
                dayOff.closeTime ?: "",
                dayOff.closed,
                dayOff.dayOfWeek,
                dayOff.openTime ?: ""
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
            val response =
                StoreMenuOptionPrice(
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

fun StoreReviewStatisticsResponse.toStoreReviewStatistics() =
    StoreReviewStatistics(
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
    BenefitCategoryList(
        this.benefitCategories.map {
            BenefitCategory(
                id = it.id,
                title = it.title ?: "",
                detail = it.detail ?: "",
                onImageUrl = it.onImageUrl ?: "",
                offImageUrl = it.offImageUrl ?: ""
            )
        }
    )

fun ShopRelatedListResponse.toShopSearchRelatedList(): ShopSearchRelatedList =
    ShopSearchRelatedList(
        keywords =
        keywords.map {
            ShopSearchRelated(
                keyword = it.keyword ?: "",
                shopIds = it.shopIds ?: emptyList(),
                shopId = it.shopId
            )
        }
    )

fun OwnerGetStoreResponse.toOwnerGetStore(): OwnerGetStore =
    OwnerGetStore(
        uid = uid ?: 0,
        name = name ?: "",
        isEvent = isEvent ?: false
    )

fun ShopResponse.toShop() = Shop(
    shopId = shopId,
    orderableShopId = orderableShopId,
    name = name,
    isDeliveryAvailable = isDeliveryAvailable,
    isTakeoutAvailable = isTakeoutAvailable,
    serviceEvent = serviceEvent,
    minimumOrderAmount = minimumOrderAmount,
    ratingAverage = ratingAverage,
    reviewCount = reviewCount,
    minimumDeliveryTip = minimumDeliveryTip,
    maximumDeliveryTip = maximumDeliveryTip,
    isOpen = isOpen,
    categoryIds = categoryIds,
    imageUrls = imageUrls,
    open = open.map {
        Shop.OrderStoreShopsOpen(
            dayOfWeek = DateFormatUtil.dayOfWeekToIndex(it.dayOfWeek),
            closed = it.closed,
            openTime = it.openTime,
            closeTime = it.closeTime
        )
    },
    openStatus = openStatus
)

fun ShopSummaryResponse.toShopSummary() = ShopSummary(
    shopId = shopId,
    orderableShopId = orderableShopId,
    name = name,
    isDeliveryAvailable = isDeliveryAvailable,
    isTakeoutAvailable = isTakeoutAvailable,
    minimumOrderAmount = minimumOrderAmount,
    ratingAverage = ratingAverage,
    reviewCount = reviewCount,
    minimumDeliveryTip = minimumDeliveryTip,
    maximumDeliveryTip = maximumDeliveryTip,
    isOpen = isOpen,
    categoryIds = categoryIds,
    images = images.map {
        ShopSummary.ShopSummaryImage(
            imageUrl = it.imageUrl,
            isThumbnail = it.isThumbnail
        )
    }
)

fun ShopDetailResponse.toShopDetail() = ShopDetail(
    shopId = shopId,
    orderableShopId = orderableShopId,
    name = name,
    address = address,
    openTime = openTime,
    closeTime = closeTime,
    closedDays = closedDays.map { DateFormatUtil.dayOfWeekToIndex(it) },
    phone = phone,
    introduction = introduction,
    notice = notice,
    deliveryTips = deliveryTips.map {
        ShopDetail.ShopDetailDeliveryTips(
            fromAmount = it.fromAmount,
            toAmount = it.toAmount,
            fee = it.fee
        )
    },
    ownerInfo = ShopDetail.ShopDetailOwnerInfo(
        name = ownerInfo.name,
        shopName = ownerInfo.shopName,
        address = ownerInfo.address,
        companyRegistrationNumber = ownerInfo.companyRegistrationNumber
    ),
    origins = origins.map {
        ShopDetail.ShopDetailOrigins(
            ingredient = it.ingredient,
            origin = it.origin
        )
    }
)

fun ShopDeliveryAvailableResponse.toShopDeliveryAvailable() = ShopDeliveryAvailable(
    campusDelivery = campusDelivery,
    offCampusDelivery = offCampusDelivery
)

fun ShopMenusResponse.toShopMenus() = ShopMenus(
    menuGroupId = menuGroupId,
    menuGroupName = menuGroupName,
    menus = menus.map { menu ->
        ShopMenus.ShopMenu(
            id = menu.id,
            name = menu.name,
            description = menu.description,
            thumbnailImage = menu.thumbnailImage,
            isSoldOut = menu.isSoldOut,
            prices = menu.prices.map { price ->
                ShopMenus.ShopMenu.ShopMenuPrice(
                    id = price.id,
                    name = price.name,
                    price = price.price
                )
            }
        )
    }
)

fun ShopMenuResponse.toShopMenu() = ShopMenu(
    id = id,
    name = name,
    description = description,
    images = images,
    isSoldOut = isSoldOut,
    prices = prices.map { price ->
        ShopMenu.ShopMenuPrice(
            id = price.id,
            name = price.name,
            price = price.price
        )
    },
    optionGroups = optionGroups.map { optionGroup ->
        ShopMenu.ShopMenuOptionGroup(
            id = optionGroup.id,
            name = optionGroup.name,
            description = optionGroup.description,
            isRequired = optionGroup.isRequired,
            minSelect = optionGroup.minSelect,
            maxSelect = optionGroup.maxSelect,
            options = optionGroup.options.map { option ->
                ShopMenu.ShopMenuOptionGroup.ShopMenuOption(
                    id = option.id,
                    name = option.name,
                    price = option.price
                )
            }
        )
    }
)

fun ShopMenusGroupResponse.toShopMenusGroup() = ShopMenusGroup(
    count = count,
    menuGroups = menuGroups.map { menuGroup ->
        ShopMenusGroup.ShopMenuGroup(
            id = menuGroup.id,
            name = menuGroup.name
        )
    }
)

fun CartItem.toCartItemRequest() = CartItemRequest(
    orderableShopMenuPriceId = orderableShopMenuPriceId,
    options = options?.map { option ->
        CartItemRequest.CartItemOptionRequest(
            optionGroupId = option.optionGroupId,
            optionId = option.optionId
        )
    }
)

fun CartAdd.toCartAddRequest() = CartAddRequest(
    orderableShopId = orderableShopId,
    orderableShopMenuId = orderableShopMenuId,
    orderableShopMenuPriceId = orderableShopMenuPriceId,
    orderableShopMenuOptionIds = orderableShopMenuOptionIds?.map { option ->
        CartAddRequest.CartAddOptionRequest(
            optionGroupId = option.optionGroupId,
            optionId = option.optionId
        )
    }
)

fun CartResponse.toCart() = Cart(
    shopName = shopName,
    shopThumbnailImageUrl = shopThumbnailImageUrl,
    orderableShopId = orderableShopId,
    isDeliveryAvailable = isDeliveryAvailable,
    isTakeoutAvailable = isTakeoutAvailable,
    shopMinimumOrderAmount = shopMinimumOrderAmount,
    items = items.map {
        Cart.CartItem(
            cartMenuItemId = it.cartMenuItemId,
            orderableShopMenuId = it.orderableShopMenuId,
            name = it.name,
            menuThumbnailImageUrl = it.menuThumbnailImageUrl,
            quantity = it.quantity,
            totalAmount = it.totalAmount,
            price = Cart.CartItem.CartPrice(
                name = it.price.name,
                price = it.price.price
            ),
            options = it.options.map { option ->
                Cart.CartItem.CartOption(
                    optionGroupName = option.optionGroupName,
                    optionName = option.optionName,
                    optionPrice = option.optionPrice
                )
            },
            isModified = it.isModified
        )
    },
    itemsAmount = itemsAmount,
    deliveryFee = deliveryFee,
    totalAmount = totalAmount,
    finalPaymentAmount = finalPaymentAmount
)

fun CartSummaryResponse.toCartSummary() = CartSummary(
    orderableShopId = orderableShopId,
    shopMinimumOrderAmount = shopMinimumOrderAmount,
    cartItemsAmount = cartItemsAmount,
    isAvailable = isAvailable
)

fun CartPaymentSummaryResponse.toCartPaymentSummary() = CartPaymentSummary(
    itemTotalAmount = itemTotalAmount,
    deliveryFee = deliveryFee,
    totalAmount = totalAmount,
    finalPaymentAmount = finalPaymentAmount
)

fun CartItemEditResponse.toCartItemEdit() = CartItemEdit(
    id = id,
    name = name,
    description = description,
    images = images,
    prices = prices.map { price ->
        CartItemEdit.CartItemEditPrice(
            id = price.id,
            name = price.name,
            price = price.price,
            isSelected = price.isSelected
        )
    },
    optionGroups = optionGroups.map { optionGroup ->
        CartItemEdit.CartItemEditOptionGroup(
            id = optionGroup.id,
            name = optionGroup.name,
            description = optionGroup.description,
            isRequired = optionGroup.isRequired,
            minSelect = optionGroup.minSelect,
            maxSelect = optionGroup.maxSelect,
            options = optionGroup.options.map { option ->
                CartItemEdit.CartItemEditOptionGroup.CartItemEditOption(
                    id = option.id,
                    name = option.name,
                    price = option.price,
                    isSelected = option.isSelected
                )
            }
        )
    }
)
