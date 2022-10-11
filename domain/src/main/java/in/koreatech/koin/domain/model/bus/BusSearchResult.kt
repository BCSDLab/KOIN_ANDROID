package `in`.koreatech.koin.domain.model.bus

import java.time.LocalDateTime

data class BusSearchResult(
    val busType: BusType,
    val busTime: LocalDateTime
)
