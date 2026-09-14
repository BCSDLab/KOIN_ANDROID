package `in`.koreatech.koin.feature.recruitment.ui.chat.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecruitmentChatMessageGroup(
    val date: String,
    val messages: ImmutableList<RecruitmentChatMessage>
)
