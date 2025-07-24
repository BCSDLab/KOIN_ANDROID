package `in`.koreatech.koin.feature.club.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.club.R

enum class EventSearchType(
    val value: String,
    @StringRes val strRes: Int
) {
    ONGOING("ONGOING", R.string.detail_events_sort_type_ongoing),
    RECENT("RECENT", R.string.detail_events_sort_type_recent),
    UPCOMING("UPCOMING", R.string.detail_events_sort_type_upcoming),
    ENDED("ENDED", R.string.detail_events_sort_type_ended)
}

val eventSearchTypeList = listOf(
    EventSearchType.ONGOING,
    EventSearchType.RECENT,
    EventSearchType.UPCOMING,
    EventSearchType.ENDED
)
