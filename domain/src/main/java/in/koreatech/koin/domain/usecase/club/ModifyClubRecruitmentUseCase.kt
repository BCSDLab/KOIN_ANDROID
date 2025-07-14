package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ModifyClubRecruitmentUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        startDate: String?,
        endDate: String?,
        isAlwaysRecruiting: Boolean,
        imageUrl: String,
        content: String
    ): Result<Unit> {
        return clubRepository.modifyClubRecruitment(
            clubId,
            startDate,
            endDate,
            isAlwaysRecruiting,
            imageUrl,
            content
        )
    }
}
