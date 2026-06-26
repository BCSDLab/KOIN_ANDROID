package `in`.koreatech.koin.domain.model.notification

import java.time.LocalDateTime

data class Notification(
    val id: Int? = null,
    val type: String,
    val datetime: LocalDateTime,
    val title: String,
    val content: String
)
