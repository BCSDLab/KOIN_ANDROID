package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class UnsubscribeClubEventUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        eventId: Int
    ): Result<Unit> {
        return clubRepository.unsubscribeClubEvent(clubId, eventId)
    }
}