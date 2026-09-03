package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentMain : RecruitmentNavType()

    @Serializable
    data class RecruitmentDetail(val postId: Int) : RecruitmentNavType()

    @Serializable
    data class RecruitmentGroupChat(
        val recruitmentId: Int,
        val chatRoomId: Int
    ) : RecruitmentNavType()

    @Serializable
    data class RecruitmentDirectChat(
        val recruitmentId: Int,
        val applicationId: Int
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
