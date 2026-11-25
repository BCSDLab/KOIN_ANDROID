package `in`.koreatech.koin.feature.chat.ui.mapper

import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.domain.model.chat.ChatDomainType

internal fun ChatDomain.toChatDomainType(): ChatDomainType = when (this) {
    ChatDomain.LOST_ITEM -> ChatDomainType.LOST_ITEM
    ChatDomain.CALLVAN -> ChatDomainType.CALLVAN
    ChatDomain.TEAM_RECRUITMENT -> ChatDomainType.TEAM_RECRUITMENT
}
