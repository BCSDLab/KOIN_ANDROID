package `in`.koreatech.koin.feature.store.orderhistory.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderHistoryTabs(@StringRes val stringRes: Int) {
    OLD_HISTORY(R.string.order_history_tab_old_history),
    IN_PROGRESS(R.string.order_history_tab_in_progress)
}
