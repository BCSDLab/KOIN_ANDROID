package `in`.koreatech.koin.feature.chat.ui.groupchat.model

import kotlinx.collections.immutable.ImmutableList


data class GroupChatMessageGroup(
    val date: String,
    val messages: ImmutableList<GroupChatMessage>
)

