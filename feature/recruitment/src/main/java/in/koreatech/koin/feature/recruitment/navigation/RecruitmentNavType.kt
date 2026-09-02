package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentMain : RecruitmentNavType()

    @Serializable
    data class RecruitmentDetail(val postId: Int) : RecruitmentNavType()

    @Serializable
    data object RecruitmentCreate : RecruitmentNavType()

    @Serializable
    data class RecruitmentApply(
        val recruitmentId: Int,
        val roles: List<RecruitmentRoleArg> = emptyList()
    ) : RecruitmentNavType()

    @Serializable
    data object Profile : RecruitmentNavType()

    @Serializable
    data class ProfileCreate(val isEditMode: Boolean = false) : RecruitmentNavType()

    @Serializable
    data class RecruitmentGroupChat(
        val postId: Int,
        val title: String,
        val currentMemberCount: Int,
        val maxMemberCount: Int,
        val date: String
    ) : RecruitmentNavType()

    @Serializable
    data class RecruitmentDirectChat(
        val postId: Int,
        val partnerNickname: String,
        val date: String
    ) : RecruitmentNavType()

    @Serializable
    data object Notification : RecruitmentNavType()

    @Serializable
    data class ApplicantManagement(val postId: Int) : RecruitmentNavType()

    @Serializable
    data class ApplicantDetail(val postId: Int, val applicantId: Int) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()

    @Serializable
    data object MyAppliedRecruitment : RecruitmentNavType()
}

@Serializable
data class RecruitmentRoleArg(
    val id: Int,
    val name: String,
    val isClosed: Boolean = false
)
