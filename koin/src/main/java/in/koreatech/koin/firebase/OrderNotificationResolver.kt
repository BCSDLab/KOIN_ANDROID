package `in`.koreatech.koin.firebase

import `in`.koreatech.koin.core.notification.NotificationPayload
import `in`.koreatech.koin.core.notification.NotificationResolver
import `in`.koreatech.koin.core.notification.NotificationType
import `in`.koreatech.koin.core.notification.ProgressNotification
import `in`.koreatech.koin.core.notification.StandardNotification
import java.net.URI
import javax.inject.Inject

class OrderNotificationResolver @Inject constructor() : NotificationResolver {
    override fun resolve(payload: NotificationPayload): NotificationType? {
        val orderId = payload.url.orderIdOrNull() ?: return null
        val state = OrderState.from(payload.title) ?: return null
        val stableKey = "order:$orderId"

        return if (state.isOngoing) {
            ProgressNotification(
                stableKey = stableKey,
                current = state.progress,
                maximum = MAX_PROGRESS
            )
        } else {
            StandardNotification(stableKey = stableKey)
        }
    }

    private fun String?.orderIdOrNull(): Int? {
        if (this == null) return null
        val uri = runCatching { URI(this) }.getOrNull() ?: return null
        if (uri.host != ORDER_HOST) return null

        return uri.rawQuery
            ?.split("&")
            ?.firstOrNull { it.substringBefore("=") == ORDER_ID_QUERY }
            ?.substringAfter("=", missingDelimiterValue = "")
            ?.toIntOrNull()
    }

    private enum class OrderState(
        val progress: Int,
        val isOngoing: Boolean
    ) {
        COOKING(progress = 33, isOngoing = true),
        DELIVERING(progress = 67, isOngoing = true),
        COMPLETED(progress = 100, isOngoing = false),
        CANCELED(progress = 0, isOngoing = false);

        companion object {
            fun from(title: String?): OrderState? = when (title) {
                "주문이 접수되었어요" -> COOKING
                "배달이 시작됐어요",
                "포장이 완료됐어요" -> DELIVERING
                "주문 수령이 완료됐어요",
                "배달이 완료됐어요" -> COMPLETED
                "주문이 취소됐어요" -> CANCELED
                else -> null
            }
        }
    }

    private companion object {
        const val ORDER_HOST = "order"
        const val ORDER_ID_QUERY = "id"
        const val MAX_PROGRESS = 100
    }
}
