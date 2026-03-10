package `in`.koreatech.koin.feature.chat.ui.groupchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class GroupChatState(
    val isLoading: Boolean = true,
    val postId: Int? = null,
    val userId: Int = 0,
    val userNickname: String = "",
    val departure: String = "",
    val arrival: String = "",
    val departureTime: String = "",
    val currentMemberCount: Int = 1,
    val maxMemberCount: Int = 8,
    val messages: ImmutableList<GroupChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = "",
    val memberColors: ImmutableMap<Int, Int> = persistentMapOf(),
    val uploadingImage: ImmutableList<ConvertedChatMessage> = persistentListOf(),
    val showImage: Pair<Boolean, String> = Pair(false, "")
)
