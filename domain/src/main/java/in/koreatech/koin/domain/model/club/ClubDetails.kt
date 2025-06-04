package `in`.koreatech.koin.domain.model.club

data class ClubDetails(
    val id: Int,
    val name: String,
    val category: String,
    val location: String,
    val imageUrl: String,
    val likes: Int,
    val description: String,
    val introduction: String,
    val instagram: String?,
    val googleForm: String?,
    val openChat: String?,
    val phoneNumber: String?,
    val manager: Boolean,
    val isLiked: Boolean,
    val updatedAt: String,
    val isLikeHidden: Boolean
)
