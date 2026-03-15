package `in`.koreatech.koin.feature.callvan.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R

enum class CallvanLocationOption(
    @StringRes val displayNameRes: Int
) {
    FRONT_GATE(R.string.callvan_location_front_gate),
    BACK_GATE(R.string.callvan_location_back_gate),
    TENNIS_COURT(R.string.callvan_location_tennis_court),
    DORMITORY_MAIN(R.string.callvan_location_dormitory_main),
    DORMITORY_SUB(R.string.callvan_location_dormitory_sub),
    TERMINAL(R.string.callvan_location_terminal),
    STATION(R.string.callvan_location_station),
    ASAN_STATION(R.string.callvan_location_asan_station),
    CUSTOM(R.string.callvan_location_custom)
}
