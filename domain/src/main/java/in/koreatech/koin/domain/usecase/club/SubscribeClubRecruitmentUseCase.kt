package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject


class SubscribeClubRecruitmentUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int
    ): Result<Unit> {
        return clubRepository.subscribeClubRecruitment(clubId)
    }
}