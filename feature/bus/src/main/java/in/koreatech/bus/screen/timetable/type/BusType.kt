package `in`.koreatech.bus.screen.timetable.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

internal enum class BusType(
    @StringRes val titleRes: Int
) {
    SHUTTLE(R.string.tab_shuttle),
    EXPRESS(R.string.tab_express),
    CITY(R.string.tab_city),
}