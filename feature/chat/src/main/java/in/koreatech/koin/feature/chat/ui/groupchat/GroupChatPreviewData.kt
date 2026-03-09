package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.net.Uri
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

internal object GroupChatPreviewData {
    data class Sender(
        val userId: Int,
        val userNickname: String
    )

    const val DEPARTURE: String = "테니스장"
    const val ARRIVAL: String = "천안터미널"
    const val LONG_DEPARTURE: String = "담헌앞 횡단보도"
    const val LONG_ARRIVAL: String = "천안시외터미널"
    const val DEPARTURE_TIME: String = "16:00"
    const val CURRENT_MEMBER_COUNT: Int = 6
    const val MAX_MEMBER_COUNT: Int = 8

    val memberColors: ImmutableMap<Int, Int> = persistentMapOf(0 to 0, 1 to 4, 2 to 2, 3 to 6)
    val emptyMemberColors: ImmutableMap<Int, Int> = persistentMapOf()
    val emptyImageState: Pair<Boolean, Uri> = false to Uri.EMPTY
    val defaultOtherSender: Sender = Sender(userId = 0, userNickname = "신짱구")

    fun messages(): ImmutableList<GroupChatMessageGroup> = persistentListOf(
        GroupChatMessageGroup(
            date = "2026년 1월 2일",
            messages = persistentListOf(
                otherMessage(
                    id = "msg_1",
                    content = "넵!!",
                    timestamp = "13:53",
                    isFirstInGroup = true
                ),
                otherMessage(
                    id = "msg_2",
                    content = "방송국 건너편인가요?",
                    timestamp = "14:53"
                ),
                myMessage(
                    id = "msg_3",
                    content = "네 맞습니다",
                    timestamp = "15:53"
                ),
                otherMessage(
                    id = "msg_4",
                    content = "5분 정도 늦을 것 같아요 ㅠㅠ",
                    timestamp = "14:53",
                    sender = Sender(userId = 2, userNickname = "이훈이"),
                    isFirstInGroup = true
                )
            )
        )
    )

    fun otherMessage(
        id: String,
        content: String,
        timestamp: String,
        sender: Sender = defaultOtherSender,
        isFirstInGroup: Boolean = false
    ): GroupChatMessage = GroupChatMessage(
        id = id,
        userId = sender.userId,
        userNickname = sender.userNickname,
        content = content,
        timestamp = timestamp,
        isSentByMe = false,
        readCount = CURRENT_MEMBER_COUNT,
        isFirstInGroup = isFirstInGroup
    )

    fun myMessage(
        id: String,
        content: String,
        timestamp: String,
        userId: Int = 1,
        userNickname: String = "나"
    ): GroupChatMessage = GroupChatMessage(
        id = id,
        userId = userId,
        userNickname = userNickname,
        content = content,
        timestamp = timestamp,
        isSentByMe = true,
        readCount = CURRENT_MEMBER_COUNT
    )
}
