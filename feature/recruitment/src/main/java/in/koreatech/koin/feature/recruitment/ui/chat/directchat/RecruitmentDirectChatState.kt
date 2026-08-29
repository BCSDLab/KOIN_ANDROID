package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentDirectChatState(
    val postId: Int = 0,
    val partnerNickname: String = "",
    val date: String = "",
    val messages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = ""
)
