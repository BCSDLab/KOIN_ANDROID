package `in`.koreatech.koin.feature.recruitment.ui.chat.model

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage as DomainRecruitmentChatMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<DomainRecruitmentChatMessage>.toRecruitmentChatMessageGroups(
    currentUserId: Int
): ImmutableList<RecruitmentChatMessageGroup> = groupBy { it.date }
    .map { (date, messagesForDate) ->
        RecruitmentChatMessageGroup(
            date = date,
            messages = messagesForDate.mapIndexed { index, message ->
                val previous = messagesForDate.getOrNull(index - 1)
                RecruitmentChatMessage(
                    id = message.messageId,
                    content = message.content,
                    timestamp = message.time,
                    isSentByMe = message.userId == currentUserId,
                    authorNickname = message.userNickname,
                    isFirstInGroup = previous?.userId != message.userId,
                    isImage = message.isImage
                )
            }.toImmutableList()
        )
    }.toImmutableList()
