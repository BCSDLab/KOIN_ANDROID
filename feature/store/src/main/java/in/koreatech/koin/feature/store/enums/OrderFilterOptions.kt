package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

interface OrderFilterEnum {
    val stringRes: Int
    val isActivated: Boolean
}

enum class LocationOption(@StringRes override val stringRes: Int, override val isActivated: Boolean) : OrderFilterEnum {
    DEFAULT(R.string.filter_option_location, false),
    CAMPUS(R.string.filter_option_location_campus, true),
    OUTSIDE(R.string.filter_option_location_outside, true)
}

enum class PeriodOption(@StringRes override val stringRes: Int, override val isActivated: Boolean) : OrderFilterEnum {
    DEFAULT(R.string.filter_option_period, false),
    THREE_MONTHS(R.string.filter_option_period_three_months, true),
    SIX_MONTHS(R.string.filter_option_period_six_months, true),
    ONE_YEAR(R.string.filter_option_period_one_year, true)
}

enum class TypeOption(@StringRes override val stringRes: Int, override val isActivated: Boolean) : OrderFilterEnum {
    DEFAULT(R.string.filter_option_type, false),
    DELIVERY(R.string.filter_option_type_delivery, true),
    TAKEOUT(R.string.filter_option_type_takeout, true)
}

enum class StatusOption(@StringRes override val stringRes: Int, override val isActivated: Boolean) : OrderFilterEnum {
    DEFAULT(R.string.filter_option_status, false),
    COMPLETE(R.string.filter_option_status_complete, true),
    CANCELED(R.string.filter_option_status_canceled, true)
}
