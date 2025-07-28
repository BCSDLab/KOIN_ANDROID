package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.club.Clubs
import `in`.koreatech.koin.feature.club.component.KoinClubRecruitmentLabelType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubItem(
    val id: Int,
    val name: String,
    val category: String,
    val likes: Int,
    val imageUrl: String,
    val isLiked: Boolean,
    val isLikeHidden: Boolean,
    val recruitmentInfo: ParcelizeClubItemRecruitmentInfo
) : Parcelable {
    @Parcelize
    data class ParcelizeClubItemRecruitmentInfo(
        val status: KoinClubRecruitmentLabelType,
        val dDay: Int?
    ) : Parcelable
}

fun Clubs.toParcelizeClubItems(): List<ParcelizeClubItem> {
    return clubs.map { club ->
        ParcelizeClubItem(
            id = club.id,
            name = club.name,
            category = club.category,
            likes = club.likes,
            imageUrl = club.imageUrl,
            isLiked = club.isLiked,
            isLikeHidden = club.isLikeHidden,
            recruitmentInfo = club.recruitmentInfo.toParcelizeClubItemRecruitmentInfo()
        )
    }
}

private fun Clubs.ClubItemRecruitmentInfo.toParcelizeClubItemRecruitmentInfo(): ParcelizeClubItem.ParcelizeClubItemRecruitmentInfo {
    return ParcelizeClubItem.ParcelizeClubItemRecruitmentInfo(
        status = when (status) {
            "NONE" -> KoinClubRecruitmentLabelType.NONE
            "RECRUITING" -> KoinClubRecruitmentLabelType.RECRUITING
            "ALWAYS" -> KoinClubRecruitmentLabelType.RECRUITMENT_ALWAYS
            "CLOSED" -> KoinClubRecruitmentLabelType.RECRUITMENT_FINISHED
            "BEFORE" -> KoinClubRecruitmentLabelType.BEFORE
            else -> KoinClubRecruitmentLabelType.NONE
        },
        dDay = this.dDay
    )
}
