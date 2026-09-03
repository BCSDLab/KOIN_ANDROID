package `in`.koreatech.koin.feature.recruitment.ui.chat.model

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage as DomainRecruitmentChatMessage
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val CHAT_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
private val CHAT_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun List<DomainRecruitmentChatMessage>.toRecruitmentChatMessageGroups(
    currentUserId: Int
): ImmutableList<RecruitmentChatMessageGroup> = groupBy { it.timestamp.toLocalDate() }
    .map { (date, messagesForDate) ->
        RecruitmentChatMessageGroup(
            date = date.format(CHAT_DATE_FORMATTER),
            messages = messagesForDate.mapIndexed { index, message ->
                val previous = messagesForDate.getOrNull(index - 1)
                RecruitmentChatMessage(
                    id = message.messageId,
                    content = message.content,
                    timestamp = message.timestamp.format(CHAT_TIME_FORMATTER),
                    isSentByMe = message.userId == currentUserId,
                    authorNickname = message.userNickname,
                    isFirstInGroup = previous?.userId != message.userId,
                    isImage = message.isImage
                )
            }.toImmutableList()
        )
    }.toImmutableList()
