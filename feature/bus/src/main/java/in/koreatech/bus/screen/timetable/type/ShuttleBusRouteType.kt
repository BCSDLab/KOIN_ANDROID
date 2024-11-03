package `in`.koreatech.bus.screen.timetable.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

enum class ShuttleBusRouteType(
    @StringRes val titleRes: Int
) {
    WEEKDAY(R.string.weekday_routes),
    WEEKEND(R.string.weekend_routes),
    CIRCULATION(R.string.circulation_routes)
}