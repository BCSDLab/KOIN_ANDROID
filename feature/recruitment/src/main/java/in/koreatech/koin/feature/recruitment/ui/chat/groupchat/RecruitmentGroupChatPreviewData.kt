package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessage
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object RecruitmentGroupChatPreviewData {
    const val TITLE = "AI 공모전 팀원 모집"
    const val CURRENT_MEMBER_COUNT = 6
    const val MAX_MEMBER_COUNT = 8
    const val DATE = "2025년 7월 22일"

    fun messages(): ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(
        RecruitmentChatMessageGroup(
            date = DATE,
            messages = listOf(
                RecruitmentChatMessage(
                    id = 1,
                    authorNickname = "낭만고양이",
                    content = "안녕하세요! 팀 합류하게 되어 반갑습니다.",
                    timestamp = "13:53",
                    isSentByMe = false,
                    isFirstInGroup = true
                ),
                RecruitmentChatMessage(
                    id = 2,
                    authorNickname = "나",
                    content = "네 반갑습니다! 잘 부탁드려요.",
                    timestamp = "13:54",
                    isSentByMe = true
                )
            ).toImmutableList()
        )
    )

    val emptyMessages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf()
}
