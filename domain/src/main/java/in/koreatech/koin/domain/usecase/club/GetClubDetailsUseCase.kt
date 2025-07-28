package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubDetailsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<ClubDetails> {
        return clubRepository.getClubDetails(clubId)
    }
}
