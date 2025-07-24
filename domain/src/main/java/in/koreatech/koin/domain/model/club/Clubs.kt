package `in`.koreatech.koin.domain.model.club

data class Clubs(
    val clubs: List<ClubItem>
) {
    data class ClubItem(
        val id: Int,
        val name: String,
        val category: String,
        val likes: Int,
        val imageUrl: String,
        val isLiked: Boolean,
        val isLikeHidden: Boolean,
        val recruitmentInfo: ClubItemRecruitmentInfo
    )

    data class ClubItemRecruitmentInfo(
        val status: String,
        val dDay: Int?
    )
}
