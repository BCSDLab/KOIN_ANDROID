package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.club.ClubRecruitment
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubRecruitment(
    val id: Int,
    val status: RecruitmentStatus,
    val dday: Int?,
    val startDate: String,
    val endDate: String,
    val imageUrl: String?,
    val content: String,
    val isManager: Boolean
) : Parcelable

fun ClubRecruitment?.toParcelizeClubRecruitment(): ParcelizeClubRecruitment? {
    if (this == null) return null
    return ParcelizeClubRecruitment(
        id = id,
        status = status.toRecruitmentStatus(),
        dday = dday,
        startDate = startDate ?: "",
        endDate = endDate ?: "",
        imageUrl = imageUrl,
        content = content ?: "",
        isManager = isManager
    )
}

enum class RecruitmentStatus {
    NONE,
    BEFORE,
    RECRUITING,
    CLOSED,
    ALWAYS,
    UNKNOWN
}

fun String.toRecruitmentStatus() = when (this) {
    "NONE" -> RecruitmentStatus.NONE
    "BEFORE" -> RecruitmentStatus.BEFORE
    "RECRUITING" -> RecruitmentStatus.RECRUITING
    "CLOSED" -> RecruitmentStatus.CLOSED
    "ALWAYS" -> RecruitmentStatus.ALWAYS
    else -> RecruitmentStatus.UNKNOWN
}
