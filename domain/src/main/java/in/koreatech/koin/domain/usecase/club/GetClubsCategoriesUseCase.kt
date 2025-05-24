package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubsCategoriesUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke() = clubRepository.getClubsCategories()
}