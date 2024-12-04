package `in`.koreatech.bus.screen.timetable.type

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ShuttleBusOperationType(
    @StringRes val titleRes: Int,
    @StringRes val simpleTitleRes: Int
) : Parcelable {
    ALL(R.string.all_routes, 0),
    WEEKDAY(R.string.weekday_routes, R.string.weekday_routes_simple),
    WEEKEND(R.string.weekend_routes, R.string.weekend_routes_simple),
    CIRCULATION(R.string.circulation_routes, R.string.circulation_routes_simple),
}