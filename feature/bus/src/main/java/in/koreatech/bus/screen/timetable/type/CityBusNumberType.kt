package `in`.koreatech.bus.screen.timetable.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

enum class CityBusNumberType(
    @StringRes val titleRes: Int
) {
    N400(R.string.n400), N405(R.string.n405), N495(R.string.n495)
}