package `in`.koreatech.koin.domain.model.recruitment

data class TeamRecruitmentApplication(
    val applicationId: Int,
    val recruitmentId: Int,
    val status: String,
    val role: TeamRecruitmentApplicationRole,
    val createdAt: String
)

data class TeamRecruitmentApplicationRole(
    val id: Int,
    val name: String
)
