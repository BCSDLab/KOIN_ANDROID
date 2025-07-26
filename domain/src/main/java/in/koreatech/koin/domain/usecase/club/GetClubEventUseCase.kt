package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubEvent
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubEventUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int, eventId: Int): Result<ClubEvent> {
        return clubRepository.getClubEvent(clubId, eventId)
    }
}
