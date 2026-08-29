package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentGroupChatState(
    val postId: Int = 0,
    val title: String = "",
    val currentMemberCount: Int = 0,
    val maxMemberCount: Int = 0,
    val date: String = "",
    val messages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = ""
)
