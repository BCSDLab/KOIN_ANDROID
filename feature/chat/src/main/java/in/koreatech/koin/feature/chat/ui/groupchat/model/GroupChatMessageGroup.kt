package `in`.koreatech.koin.feature.chat.ui.groupchat.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class GroupChatMessageGroup(
    val date: String,
    val messages: ImmutableList<GroupChatMessage>
)
