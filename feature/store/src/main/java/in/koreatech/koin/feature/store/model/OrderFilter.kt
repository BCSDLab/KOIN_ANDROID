package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption

data class OrderFilter(
    val period: PeriodOption,
    val type: TypeOption,
    val status: StatusOption
)
