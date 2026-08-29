package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessage
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object RecruitmentDirectChatPreviewData {
    const val PARTNER_NICKNAME = "낭만고양이"
    const val DATE = "2025년 7월 22일"

    fun messages(): ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(
        RecruitmentChatMessageGroup(
            date = DATE,
            messages = listOf(
                RecruitmentChatMessage(
                    id = "1",
                    content = "안녕하세요! 지원서 잘 봤습니다.",
                    timestamp = "13:53",
                    isSentByMe = false
                ),
                RecruitmentChatMessage(
                    id = "2",
                    content = "감사합니다! 잘 부탁드려요.",
                    timestamp = "13:54",
                    isSentByMe = true
                )
            ).toImmutableList()
        )
    )

    val emptyMessages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf()
}
