package `in`.koreatech.koin.domain.model.bus.search

import `in`.koreatech.koin.domain.model.bus.BusType
import java.time.LocalDateTime

data class BusSearchResult(
    val busType: BusType,
    val busTimeString: String
)
