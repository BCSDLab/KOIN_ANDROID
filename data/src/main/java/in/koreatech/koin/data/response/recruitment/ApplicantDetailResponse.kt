package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class ApplicantDetailResponse(
    @SerializedName("application_id")
    val applicationId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("profile_snapshot")
    val profileSnapshot: ProfileSnapshotResponse,
    @SerializedName("motivation")
    val motivation: String,
    @SerializedName("availability")
    val availability: String,
    @SerializedName("role")
    val role: ApplicationRoleResponse?,
    @SerializedName("can_decide")
    val canDecide: Boolean,
    @SerializedName("can_open_direct_chat")
    val canOpenDirectChat: Boolean
)

data class ProfileSnapshotResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("student_year")
    val studentYear: Int,
    @SerializedName("preferred_role")
    val preferredRole: String,
    @SerializedName("skills")
    val skills: List<String>,
    @SerializedName("activities")
    val activities: List<TeamRecruitmentActivityResponse>,
    @SerializedName("self_introduction")
    val selfIntroduction: String
)
