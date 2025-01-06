package `in`.koreatech.bus.type

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import `in`.koreatech.koin.feature.bus.R

// property 이름 바꾸면 안됨!!
enum class BusType(
    @StringRes val titleRes: Int
) {
    ALL(R.string.all_bus_type),
    SHUTTLE(R.string.tab_shuttle),
    EXPRESS(R.string.tab_express),
    CITY(R.string.tab_city);

    @Composable
    @ReadOnlyComposable
    fun getEventValue(): String {
        return when(this) {
            SHUTTLE -> stringResource(R.string.shuttle_timetable)
            EXPRESS -> stringResource(R.string.express_timetable)
            CITY -> stringResource(R.string.city_timetable)
            else -> ""
        }
    }

    companion object {
        val entriesExceptAll = values().filter { it != ALL }
    }
}