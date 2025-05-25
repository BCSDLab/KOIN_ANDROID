package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.Clubs
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<Clubs> {
        return clubRepository.getClubList(clubId)
    }
}
