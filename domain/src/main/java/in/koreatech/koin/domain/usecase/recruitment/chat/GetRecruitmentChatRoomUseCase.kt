package `in`.koreatech.koin.domain.usecase.recruitment.chat

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoom
import `in`.koreatech.koin.domain.repository.RecruitmentChatRepository
import javax.inject.Inject

class GetRecruitmentChatRoomUseCase @Inject constructor(
    private val recruitmentChatRepository: RecruitmentChatRepository
) {
    suspend operator fun invoke(recruitmentId: Int, chatRoomId: Int): Result<RecruitmentChatRoom> =
        recruitmentChatRepository.getChatRoom(recruitmentId, chatRoomId)
}
