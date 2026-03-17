package `in`.koreatech.koin.feature.callvan.ui

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.create.model.CallvanLocationOption

@StringRes
fun CallvanLocationOption.displayNameRes(): Int = when (this) {
    CallvanLocationOption.FRONT_GATE -> R.string.callvan_location_front_gate
    CallvanLocationOption.BACK_GATE -> R.string.callvan_location_back_gate
    CallvanLocationOption.TENNIS_COURT -> R.string.callvan_location_tennis_court
    CallvanLocationOption.DORMITORY_MAIN -> R.string.callvan_location_dormitory_main
    CallvanLocationOption.DORMITORY_SUB -> R.string.callvan_location_dormitory_sub
    CallvanLocationOption.TERMINAL -> R.string.callvan_location_terminal
    CallvanLocationOption.STATION -> R.string.callvan_location_station
    CallvanLocationOption.ASAN_STATION -> R.string.callvan_location_asan_station
    CallvanLocationOption.CUSTOM -> R.string.callvan_location_custom
}
