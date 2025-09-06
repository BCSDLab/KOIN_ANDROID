package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderStatus(@StringRes val stringRes: Int, val isActivated: Boolean) {
    CANCELLED(R.string.order_history_status_canceled, false), // 주문 취소
    DELIVERED(R.string.order_history_status_delivered, true), // 배달 완료
    TAKEOUT_DONE(R.string.order_history_status_takeout_done, true) // 포장 완료
}
