package `in`.koreatech.koin.feature.chat.navigation

import `in`.koreatech.koin.core.navigation.utils.EXTRA_APPLICATION_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_CONTEXT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_DOMAIN_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class ChatNavType {
    @Serializable
    data class ChatList(val showBlockedMessage: Boolean = false) : ChatNavType()

    @Serializable
    data class Chat(
        @SerialName(EXTRA_CHAT_DOMAIN_TYPE) val domainType: String,
        @SerialName(EXTRA_CHAT_CONTEXT_ID) val contextId: Int,
        @SerialName(EXTRA_CHAT_ROOM_ID) val chatRoomId: Int? = null,
        @SerialName(EXTRA_APPLICATION_ID) val applicationId: Int? = null
    ) : ChatNavType()

    @Serializable
    data class GroupChat(
        @SerialName(EXTRA_CHAT_DOMAIN_TYPE) val domainType: String,
        @SerialName(EXTRA_CHAT_CONTEXT_ID) val contextId: Int,
        @SerialName(EXTRA_CHAT_ROOM_ID) val chatRoomId: Int? = null
    ) : ChatNavType()
}
