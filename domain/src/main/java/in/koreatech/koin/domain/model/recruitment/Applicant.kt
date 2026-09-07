package `in`.koreatech.koin.domain.model.recruitment

data class ApplicationRole(
    val id: Int,
    val name: String
)

data class ApplicantRecruitment(
    val id: Int,
    val category: String,
    val title: String,
    val meetingType: String,
    val activityStartDate: String,
    val activityEndDate: String,
    val deadlineDate: String,
    val dDay: Int?,
    val status: String,
    val recruitmentType: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val roles: List<RecruitmentRole>,
    val teamChatAvailable: Boolean,
    val teamChatRoomId: Int?
)

data class ApplicantSummary(
    val applicationId: Int,
    val nickname: String,
    val department: String,
    val studentYear: Int,
    val role: ApplicationRole?,
    val status: String,
    val canOpenDirectChat: Boolean
)

data class ApplicantList(
    val recruitment: ApplicantRecruitment,
    val applications: List<ApplicantSummary>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)

data class ApplicantProfileSnapshot(
    val nickname: String,
    val department: String,
    val studentYear: Int,
    val preferredRole: String,
    val skills: List<String>,
    val activities: List<TeamRecruitmentActivity>,
    val selfIntroduction: String
)

data class ApplicantDetail(
    val applicationId: Int,
    val status: String,
    val profileSnapshot: ApplicantProfileSnapshot,
    val motivation: String,
    val availability: String,
    val role: ApplicationRole?,
    val canDecide: Boolean,
    val canOpenDirectChat: Boolean
)
