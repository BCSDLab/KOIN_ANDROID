package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ModifyClubUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        name: String,
        imageUrl: String,
        clubCategoryId: Int,
        location: String,
        description: String,
        instagram: String,
        googleForm: String,
        openChat: String,
        phoneNumber: String,
        isLikeHidden: Boolean
    ): Result<Unit> {
        return clubRepository.modifyClub(
            clubId = clubId,
            name = name,
            imageUrl = imageUrl,
            clubCategoryId = clubCategoryId,
            location = location,
            description = description.ifEmpty { null },
            instagram = instagram.ifEmpty { null },
            googleForm = googleForm.ifEmpty { null },
            openChat = openChat.ifEmpty { null },
            phoneNumber = phoneNumber,
            isLikeHidden = isLikeHidden
        )
    }
}
