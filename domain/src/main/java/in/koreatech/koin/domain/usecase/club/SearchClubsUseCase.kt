package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.model.club.ClubSearch
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class SearchClubsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(query: String): Result<ClubSearch> {
        return clubRepository.searchClubs(query)
    }
}
