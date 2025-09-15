package `in`.koreatech.koin.feature.store.orderhistory.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderStatus(@StringRes val stringRes: Int) {
    COMPLETED(R.string.order_status_completed),
    CANCELED(R.string.order_status_canceled)
}
