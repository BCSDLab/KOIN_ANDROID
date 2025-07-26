package `in`.koreatech.koin.domain.model.club

data class ClubRecruitment(
    val id: Int,
    val status: String,
    val dday: Int?,
    val startDate: String?,
    val endDate: String?,
    val imageUrl: String?,
    val content: String?,
    val isManager: Boolean
)
