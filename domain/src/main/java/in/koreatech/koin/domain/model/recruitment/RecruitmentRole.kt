package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentRole(
    val id: Int,
    val name: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val isClosed: Boolean
)
