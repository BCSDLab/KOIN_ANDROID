package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.ordershop.OrderMenuListResponse
import `in`.koreatech.koin.data.ordershop.OrderMenuResponse
import `in`.koreatech.koin.data.ordershop.OrderShopResponse
import `in`.koreatech.koin.data.ordershop.ShopOriginResponse
import `in`.koreatech.koin.domain.model.ordershop.OrderMenu
import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary

fun OrderMenuListResponse.toOrderMenuList() = OrderMenuList(
    menuGroupId = menuGroupId,
    menuGroupName = menuGroupName,
    menus = menus.map { it.toOrderMenu() }
)

fun OrderMenuResponse.toOrderMenu() = OrderMenu(
    id = menuId,
    name = menuName,
    description = description ?: "",
    thumbnailImage = thumbnailImage ?: "",
    isSoldOut = isSoldOut,
    prices = prices.map { it.toOrderMenuPrice() }
)

fun OrderMenuResponse.Price.toOrderMenuPrice() = OrderMenu.Price(
    id = priceId,
    name = priceName,
    price = price
)

fun OrderShopResponse.toOrderShop() = OrderShopSummary(
    shopId = shopId,
    orderableShopId = orderableShopId,
    name = name,
    isDeliveryAvailable = isDeliveryAvailable,
    isTakeoutAvailable = isTakeoutAvailable,
    payCard = payCard,
    payBank = payBank,
    minimumOrderAmount = minimumOrderAmount,
    ratingAverage = ratingAverage,
    reviewCount = reviewCount,
    minimumDeliveryTip = minimumDeliveryTip,
    maximumDeliveryTip = maximumDeliveryTip,
    images = ShopImages.map { it.imageUrl }
)

fun ShopOriginResponse.toShopOrigin() = OrderShop(
    shopId = shopId,
    orderableShopId = orderableShopId,
    name = name,
    address = address,
    openTime = openTime ?: "",
    closeTime = closeTime ?: "",
    closedDays = closedDays,
    phone = phone,
    introduction = introduction ?: "",
    notice = notice ?: "",
    deliveryTips = deliveryTips.map { OrderShop.DeliveryTip(it.toAmount, it.fromAmount, it.fee) },
    ownerInfo = ownerInfoResponse.toOwnerInfo(),
    origins = origins.map { it.toOrigin() }
)

fun ShopOriginResponse.OwnerInfoResponse.toOwnerInfo() = OrderShop.OwnerInfo(
    name = name,
    shopName = shopName,
    address = address,
    companyRegistrationNumber = companyRegistrationNumber
)

fun ShopOriginResponse.Origin.toOrigin() = OrderShop.Origin(
    ingredients = ingredients,
    origin = origin
)
