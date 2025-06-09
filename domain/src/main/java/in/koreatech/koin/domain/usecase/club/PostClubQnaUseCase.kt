package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class PostClubQnaUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        parentId: Int?,
        content: String
    ): Result<Unit> {
        return clubRepository.postClubQna(clubId, parentId, content)
    }
}
