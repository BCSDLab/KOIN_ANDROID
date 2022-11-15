package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import java.time.format.DateTimeFormatter

data class BusSearchResultItem(
    val busType: BusType,
    val time: String
)

fun BusSearchResult.toBusSearchResultItem() = BusSearchResultItem(
    busType = busType,
    time = busTimeString
)