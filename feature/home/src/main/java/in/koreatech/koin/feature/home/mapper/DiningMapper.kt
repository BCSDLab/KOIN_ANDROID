package `in`.koreatech.koin.feature.home.mapper

import `in`.koreatech.koin.domain.model.dining.DiningWithOperationTime
import `in`.koreatech.koin.feature.home.component.DiningPagerData
import kotlinx.collections.immutable.toImmutableList

fun DiningWithOperationTime.toDiningPagerDataList(): DiningPagerData = DiningPagerData(
    place = place,
    startTime = startTime,
    endTime = endTime,
    price = priceCard.toIntOrNull() ?: priceCash.toIntOrNull() ?: 0,
    kcal = kcal.toIntOrNull() ?: 0,
    menu = menu.toImmutableList()
)
