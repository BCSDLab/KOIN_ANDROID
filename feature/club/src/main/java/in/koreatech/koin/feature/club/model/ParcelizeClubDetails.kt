package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.club.ClubDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubDetails(
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
    val updatedAt: String
) : Parcelable

fun ClubDetails?.toParcelizeClubDetails(): ParcelizeClubDetails? {
    if (this == null) return null
    return ParcelizeClubDetails(
        id = id,
        name = name,
        category = category,
        location = location,
        imageUrl = imageUrl,
        likes = likes,
        description = description,
        introduction = introduction,
        instagram = instagram,
        googleForm = googleForm,
        openChat = openChat,
        phoneNumber = phoneNumber,
        manager = manager,
        isLiked = isLiked,
        updatedAt = updatedAt
    )
}