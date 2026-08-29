package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoomStatus
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentGroupChatState(
    val recruitmentId: Int = 0,
    val chatRoomId: Int = 0,
    val title: String = "",
    val currentMemberCount: Int = 0,
    val maxMemberCount: Int = 0,
    val status: RecruitmentChatRoomStatus = RecruitmentChatRoomStatus.ACTIVE,
    val isLoading: Boolean = true,
    val currentUserId: Int = 0,
    val lastMessageId: Int? = null,
    val messages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = "",
    val isUploadingImage: Boolean = false
)
