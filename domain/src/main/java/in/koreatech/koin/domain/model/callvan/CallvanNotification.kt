package `in`.koreatech.koin.domain.model.callvan

data class CallvanNotification(
    val id: Int,
    val type: String,
    val messagePreview: String,
    val isRead: Boolean,
    val createdAt: String,
    val postId: Int,
    val departure: String,
    val arrival: String,
    val departureDate: String,
    val departureTime: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val senderNickname: String?,
    val joinedMemberNickname: String?
)
