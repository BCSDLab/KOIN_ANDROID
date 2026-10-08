package `in`.koreatech.koin.feature.dining.mapper

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningWithOperationTime

fun DiningWithOperationTime.toDining() = Dining(
    id = id,
    date = date,
    type = type,
    place = place,
    priceCard = priceCard,
    priceCash = priceCash,
    kcal = kcal,
    menu = menu,
    imageUrl = imageUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    soldOutAt = soldOutAt,
    changedAt = changedAt
)