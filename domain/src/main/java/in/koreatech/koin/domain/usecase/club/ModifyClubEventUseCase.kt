package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ModifyClubEventUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        eventId: Int,
        name: String,
        imageUrls: List<String>,
        startDate: String,
        endDate: String,
        introduce: String,
        content: String?
    ): Result<Unit> {
        return clubRepository.modifyClubEvent(
            clubId,
            eventId,
            name,
            imageUrls,
            startDate,
            endDate,
            introduce,
            content
        )
    }
}