package `in`.koreatech.koin.feature.recruitment.ui.chat.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecruitmentChatMessageGroup(
    val date: LocalDate,
    val messages: ImmutableList<RecruitmentChatMessage>
)
