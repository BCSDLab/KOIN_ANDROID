package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubEvent
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubEventsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int, eventType: String): Result<List<ClubEvent>> {
        return clubRepository.getClubEvents(clubId, eventType)
    }
}
