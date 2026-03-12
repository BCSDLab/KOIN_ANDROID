package `in`.koreatech.koin.feature.callvan.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R

enum class CallvanLocationOption(
    @StringRes val displayNameRes: Int,
    val type: String
) {
    FRONT_GATE(R.string.callvan_location_front_gate, "FRONT_GATE"),
    BACK_GATE(R.string.callvan_location_back_gate, "BACK_GATE"),
    TENNIS_COURT(R.string.callvan_location_tennis_court, "TENNIS_COURT"),
    DORMITORY_MAIN(R.string.callvan_location_dormitory_main, "DORMITORY_MAIN"),
    DORMITORY_SUB(R.string.callvan_location_dormitory_sub, "DORMITORY_SUB"),
    TERMINAL(R.string.callvan_location_terminal, "TERMINAL"),
    STATION(R.string.callvan_location_station, "STATION"),
    ASAN_STATION(R.string.callvan_location_asan_station, "ASAN_STATION"),
    CUSTOM(R.string.callvan_location_custom, "CUSTOM")
}
