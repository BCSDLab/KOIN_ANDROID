package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderHistoryStatus(@StringRes val stringRes: Int, val isActivated: Boolean) {
    CANCELLED(R.string.order_history_status_canceled, false), // 주문 취소
    DELIVERED(R.string.order_history_status_delivered, true), // 배달 완료
    TAKEOUT_DONE(R.string.order_history_status_takeout_done, true) // 포장 완료
}

enum class OrderOnGoingStatus(@StringRes val stringRes: Int, @StringRes val stringResMessage: Int, val showTime: Boolean = false) {
    CONFIRMING(R.string.order_ongoing_status_confirming, R.string.order_ongoing_status_confirming_message),
    COOKING(R.string.order_ongoing_status_cooking, R.string.order_ongoing_status_cooking_message, true),
    PACKAGED(R.string.order_ongoing_status_packaged, R.string.order_ongoing_status_packaged_message),

//    PICKED_UP(R.string.order_ongoing_status_picked_up),
    DELIVERING(R.string.order_ongoing_status_delivering, R.string.order_ongoing_status_delivering_message, true),
    DELIVERED(R.string.order_ongoing_status_delivered, R.string.order_ongoing_status_delivered_message)
//    CANCELED(R.string.order_ongoing_status_)
}
