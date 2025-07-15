package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

    class SetClubEmpowermentUseCase @Inject constructor(
        private val clubRepository: ClubRepository
    ) {
        suspend operator fun invoke(
            clubId: Int,
            changedManagerId: String
        ): Result<Unit> {
            return clubRepository.setClubEmpowerment(clubId, changedManagerId)
        }
    }
