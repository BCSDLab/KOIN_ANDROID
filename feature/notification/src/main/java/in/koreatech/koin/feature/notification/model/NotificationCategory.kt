package `in`.koreatech.koin.feature.notification.model

import `in`.koreatech.koin.feature.notification.R

internal enum class NotificationCategory(val drawableRes: Int?, val analyticsLabel: String) {
    SHOP(R.drawable.ic_notification_store, "상점"),
    DINING(R.drawable.ic_notification_dining, "식단"),
    LOST_ITEM(R.drawable.ic_notification_lostitem, "분실물"),
    CHAT(R.drawable.ic_notification_chat, "쪽지"),
    CALLVAN(R.drawable.ic_notification_callvan, "콜밴팟"),

    // 백엔드가 신규 type을 추가하면 이 enum에 항목을 추가한다.
    UNKNOWN(drawableRes = null, analyticsLabel = "기타");

    companion object {
        fun from(type: String): NotificationCategory = when (type) {
            "shop" -> SHOP
            "dining" -> DINING
            "lost-item" -> LOST_ITEM
            "chat" -> CHAT
            "callvan", "callvan-chat" -> CALLVAN
            else -> UNKNOWN
        }
    }
}
