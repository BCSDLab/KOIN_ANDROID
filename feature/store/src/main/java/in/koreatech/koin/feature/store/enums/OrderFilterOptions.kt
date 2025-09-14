package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class PeriodOption(@StringRes val stringRes: Int, val isDefault: Boolean = false) {
    NONE(R.string.filter_option_period, true),
    LAST_3_MONTHS(R.string.filter_option_period_three_months),
    LAST_6_MONTHS(R.string.filter_option_period_six_months),
    LAST_1_YEAR(R.string.filter_option_period_one_year)
}

enum class TypeOption(@StringRes val stringRes: Int, val isDefault: Boolean = false) {
    NONE(R.string.filter_option_type, true),
    DELIVERY(R.string.filter_option_type_delivery),
    TAKE_OUT(R.string.filter_option_type_takeout)
}

enum class StatusOption(@StringRes val stringRes: Int, val isDefault: Boolean = false) {
    NONE(R.string.filter_option_status, true),
    COMPLETED(R.string.filter_option_status_complete),
    CANCELED(R.string.filter_option_status_canceled)
}
