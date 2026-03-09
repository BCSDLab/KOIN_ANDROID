package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.net.Uri
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

internal const val PREVIEW_DEPARTURE: String = "테니스장"
internal const val PREVIEW_ARRIVAL: String = "천안터미널"
internal const val PREVIEW_LONG_DEPARTURE: String = "담헌앞 횡단보도"
internal const val PREVIEW_LONG_ARRIVAL: String = "천안시외터미널"
internal const val PREVIEW_DEPARTURE_TIME: String = "16:00"
internal const val PREVIEW_CURRENT_MEMBER_COUNT: Int = 6
internal const val PREVIEW_MAX_MEMBER_COUNT: Int = 8

internal val previewMemberColors: ImmutableMap<Int, Int> = persistentMapOf(0 to 0, 1 to 4, 2 to 2, 3 to 6)
internal val previewEmptyMemberColors: ImmutableMap<Int, Int> = persistentMapOf()

internal val previewEmptyImageState: Pair<Boolean, Uri> = Pair(false, Uri.EMPTY)

internal fun previewMessages(): ImmutableList<GroupChatMessageGroup> = persistentListOf(
    GroupChatMessageGroup(
        date = "2026년 1월 2일",
        messages = persistentListOf(
            previewMessage(
                id = "msg_1",
                userId = 0,
                userNickname = "신짱구",
                content = "넵!!",
                timestamp = "13:53",
                isFirstInGroup = true
            ),
            previewMessage(
                id = "msg_2",
                userId = 0,
                userNickname = "신짱구",
                content = "방송국 건너편인가요?",
                timestamp = "14:53",
                isFirstInGroup = false
            ),
            previewMessage(
                id = "msg_3",
                userId = 1,
                userNickname = "나",
                content = "네 맞습니다",
                timestamp = "15:53",
                isSentByMe = true
            ),
            previewMessage(
                id = "msg_4",
                userId = 2,
                userNickname = "이훈이",
                content = "5분 정도 늦을 것 같아요 ㅠㅠ",
                timestamp = "14:53",
                isFirstInGroup = true
            )
        )
    )
)

internal fun previewMessage(
    id: String,
    userId: Int,
    userNickname: String,
    content: String,
    timestamp: String,
    isSentByMe: Boolean = false,
    isFirstInGroup: Boolean = false
): GroupChatMessage = GroupChatMessage(
    id = id,
    userId = userId,
    userNickname = userNickname,
    content = content,
    timestamp = timestamp,
    isSentByMe = isSentByMe,
    readCount = PREVIEW_CURRENT_MEMBER_COUNT,
    isFirstInGroup = isFirstInGroup
)
