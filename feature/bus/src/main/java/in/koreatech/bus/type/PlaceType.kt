package `in`.koreatech.bus.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.serialization.Serializable

@Serializable
enum class PlaceType(
    @StringRes val titleRes: Int,
) {
    KOREATECH(R.string.koreatech),
    STATION(R.string.cheonan_station),
    TERMINAL(R.string.cheonan_terminal),
}
