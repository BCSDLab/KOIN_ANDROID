package `in`.koreatech.koin.feature.store.orderhistory.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderStatus(@StringRes val stringRes: Int) {
    CONFIRMING(R.string.order_status_confirming),
    COOKING(R.string.order_status_cooking),
    PACKAGED(R.string.order_status_packaged),
    PICKED_UP(R.string.order_status_picked_up),
    DELIVERING(R.string.order_status_delivering),
    DELIVERED(R.string.order_status_delivered),
    CANCELED(R.string.order_status_canceled),
}
