package `in`.koreatech.koin.feature.store.orderhistory.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderHistoryPeriod(@StringRes val stringRes: Int) {
    LAST_3_MONTHS(R.string.order_history_period_three_months),
    LAST_6_MONTHS(R.string.order_history_period_six_months),
    LAST_1_YEAR(R.string.order_history_period_one_year)
}
