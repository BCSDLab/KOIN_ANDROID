package `in`.koreatech.koin.feature.store.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderType(@StringRes val stringRes: Int) {
    DELIVERY(R.string.order_type_delivery),
    TAKE_OUT(R.string.order_type_pickup)
}
