package `in`.koreatech.bus.screen.search.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

internal enum class PlaceType(
    @StringRes val titleRes: Int
) {
    KOREATECH(R.string.koreatech),
    CHEONAN_STATION(R.string.cheonan_station),
    CHEONAN_TERMINAL(R.string.cheonan_terminal),
}