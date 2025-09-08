package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption

data class OrderFilter(
    val location: LocationOption,
    val period: PeriodOption,
    val type: TypeOption,
    val status: StatusOption
) {
    fun isAllDefault() = listOf(location, period, type, status).all { it.isDefault }
}
