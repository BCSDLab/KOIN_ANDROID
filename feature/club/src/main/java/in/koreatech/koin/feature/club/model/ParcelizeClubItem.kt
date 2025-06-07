package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.club.Clubs
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubItem(
    val id: Int,
    val name: String,
    val category: String,
    val likes: Int,
    val imageUrl: String,
    val isLiked: Boolean,
    val isLikeHidden: Boolean
) : Parcelable

fun Clubs.toParcelizeClubItems(): List<ParcelizeClubItem> {
    return clubs.map { club ->
        ParcelizeClubItem(
            id = club.id,
            name = club.name,
            category = club.category,
            likes = club.likes,
            imageUrl = club.imageUrl,
            isLiked = club.isLiked,
            isLikeHidden = club.isLikeHidden
        )
    }
}
