package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class DeleteClubQnaUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        qnaId: Int
    ): Result<Unit> {
        return clubRepository.deleteClubQna(clubId, qnaId)
    }
}
