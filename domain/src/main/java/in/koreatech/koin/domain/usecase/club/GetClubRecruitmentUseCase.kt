package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubRecruitment
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubRecruitmentUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<ClubRecruitment> {
        return clubRepository.getClubRecruitment(clubId)
    }
}
