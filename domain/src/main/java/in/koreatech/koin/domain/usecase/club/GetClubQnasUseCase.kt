package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubQnasUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<ClubQnasInfo> {
        return clubRepository.getClubQnas(clubId)
    }
}
