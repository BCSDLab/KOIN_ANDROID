package `in`.koreatech.koin.feature.store.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.store.R

@Immutable
enum class OrderStatus(@StringRes val stringRes: Int) {
    NONE(0),
    CONFIRMING(R.string.order_status_confirming),
    COOKING(R.string.order_status_cooking),
    PACKAGED(R.string.order_status_packaged),
    PICKED_UP(R.string.order_status_picked_up),
    DELIVERING(R.string.order_status_delivering),
    DELIVERED(R.string.order_status_delivered),
    CANCELED(R.string.order_status_canceled);

    companion object {
        fun fromString(value: String) = when (value) {
            "CONFIRMING" -> CONFIRMING
            "COOKING" -> COOKING
            "PACKAGED" -> PACKAGED
            "PICKED_UP" -> PICKED_UP
            "DELIVERING" -> DELIVERING
            "DELIVERED" -> DELIVERED
            "CANCELED" -> CANCELED
            else -> NONE
        }
    }
}
