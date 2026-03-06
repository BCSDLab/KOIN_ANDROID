package `in`.koreatech.koin.domain.model.callvan

data class CallvanPostDetail(
    val id: Int,
    val title: String,
    val departure: String,
    val arrival: String,
    val departureDate: String,
    val departureTime: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val status: String,
    val participants: List<CallvanParticipant>
) {
    data class CallvanParticipant(
        val userId: Int,
        val nickname: String,
        val isMe: Boolean
    )
}
