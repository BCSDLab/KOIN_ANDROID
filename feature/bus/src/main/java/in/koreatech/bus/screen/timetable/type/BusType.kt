package `in`.koreatech.bus.screen.timetable.type

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BusType(
    @StringRes val titleRes: Int
) : Parcelable {
    SHUTTLE(R.string.tab_shuttle),
    EXPRESS(R.string.tab_express),
    CITY(R.string.tab_city),
}