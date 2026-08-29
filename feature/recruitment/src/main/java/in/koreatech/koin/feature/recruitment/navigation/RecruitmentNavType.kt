package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
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
    data class ApplicantManagement(val postId: Long) : RecruitmentNavType()

    @Serializable
    data class ApplicantDetail(val postId: Long, val applicantId: Long) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()
}
