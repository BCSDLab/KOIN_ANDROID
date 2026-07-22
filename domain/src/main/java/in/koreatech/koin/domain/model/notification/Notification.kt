package `in`.koreatech.koin.domain.model.notification

import java.time.LocalDateTime

data class Notification(
    val id: Int = 0,
    val type: String,
    val datetime: LocalDateTime,
    val title: String,
    val content: String,
    val originUrl: String,
    val isRead: Boolean
)
