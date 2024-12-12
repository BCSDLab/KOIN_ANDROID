package `in`.koreatech.bus.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

// property 이름 바꾸면 안됨!!
enum class BusType(
    @StringRes val titleRes: Int
) {
    ALL(R.string.all_bus_type),
    SHUTTLE(R.string.tab_shuttle),
    EXPRESS(R.string.tab_express),
    CITY(R.string.tab_city);

    companion object {
        val entriesExceptAll = values().filter { it != ALL }
    }
}