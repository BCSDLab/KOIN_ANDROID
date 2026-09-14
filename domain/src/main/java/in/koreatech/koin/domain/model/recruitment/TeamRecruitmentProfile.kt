package `in`.koreatech.koin.domain.model.recruitment

data class TeamRecruitmentProfile(
    val profileNickname: String,
    val department: String,
    val major: String?,
    val studentNumber: String,
    val preferredRole: String,
    val skills: List<String>,
    val activities: List<TeamRecruitmentActivity>,
    val selfIntroduction: String
)

data class TeamRecruitmentActivity(
    val id: Int,
    val title: String,
    val startedAt: String,
    val endedAt: String?,
    val isOngoing: Boolean,
    val description: String
)

data class TeamRecruitmentActivityInput(
    val title: String,
    val startedAt: String,
    val endedAt: String?,
    val isOngoing: Boolean,
    val description: String
)
