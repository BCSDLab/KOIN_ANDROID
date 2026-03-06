package `in`.koreatech.koin.domain.model.callvan

data class CallvanChatMessage(
    val roomName: String,
    val messages: List<CallvanMessage>
) {
    data class CallvanMessage(
        val userId: Int,
        val senderNickname: String,
        val content: String,
        val date: String,
        val time: String,
        val isImage: Boolean,
        val isLeftUser: Boolean,
        val isMine: Boolean
    )
}
