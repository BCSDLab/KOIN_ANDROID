package `in`.koreatech.koin.feature.store.orderhistory.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderType(@StringRes val stringRes: Int) {
    DELIVERY(R.string.order_type_delivery),
    PICKUP(R.string.order_type_pickup)
}
