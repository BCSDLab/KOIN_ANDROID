package `in`.koreatech.koin.feature.chat.ui.groupchat

import `in`.koreatech.koin.feature.chat.ui.model.ChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessageGroup
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object GroupChatPreviewData {
    data class Sender(
        val userId: Int,
        val nickname: String
    )

    const val DEPARTURE: String = "테니스장"
    const val ARRIVAL: String = "천안터미널"
    const val DEPARTURE_TIME: String = "16:00"
    const val CURRENT_MEMBER_COUNT: Int = 6
    const val MAX_MEMBER_COUNT: Int = 8

    private val defaultOtherSender = Sender(userId = 0, nickname = "신짱구")

    fun messages(): ImmutableList<ChatMessageGroup> = persistentListOf(
        ChatMessageGroup(
            date = LocalDate.of(2026, 1, 2),
            messages = persistentListOf(
                otherMessage("msg_1", "넵!!", "13:53", isFirstInGroup = true),
                otherMessage("msg_2", "방송국 건너편인가요?", "14:53"),
                myMessage("msg_3", "네 맞습니다", "15:53"),
                otherMessage(
                    "msg_4",
                    "5분 정도 늦을 것 같아요 ㅠㅠ",
                    "14:53",
                    sender = Sender(userId = 2, nickname = "이훈이"),
                    isFirstInGroup = true
                )
            )
        )
    )

    private fun otherMessage(
        id: String,
        content: String,
        timestamp: String,
        sender: Sender = defaultOtherSender,
        isFirstInGroup: Boolean = false
    ): ChatMessage = ChatMessage(
        id = id,
        content = content,
        timestamp = timestamp,
        isSentByMe = false,
        authorNickname = sender.nickname,
        isFirstInGroup = isFirstInGroup,
        userId = sender.userId
    )

    private fun myMessage(
        id: String,
        content: String,
        timestamp: String,
        userId: Int = 1
    ): ChatMessage = ChatMessage(
        id = id,
        content = content,
        timestamp = timestamp,
        isSentByMe = true,
        userId = userId
    )
}
