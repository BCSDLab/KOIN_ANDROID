package `in`.koreatech.koin.domain.usecase.club

import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class CreateClubUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        name: String,
        imageUrl: String,
        clubManagers: List<String>,
        clubCategoryId: Int,
        location: String,
        description: String,
        instagram: String,
        googleForm: String,
        openChat: String,
        phoneNumber: String,
        role: String,
        isLikeHidden: Boolean
    ): Result<Unit> {
        return clubRepository.createClub(
            name = name,
            imageUrl = imageUrl,
            clubManagers = clubManagers,
            clubCategoryId = clubCategoryId,
            location = location,
            description = description.ifEmpty { null },
            instagram = instagram.ifEmpty { null },
            googleForm = googleForm.ifEmpty { null },
            openChat = openChat.ifEmpty { null },
            phoneNumber = phoneNumber,
            role = role,
            isLikeHidden = isLikeHidden
        )
    }
}
