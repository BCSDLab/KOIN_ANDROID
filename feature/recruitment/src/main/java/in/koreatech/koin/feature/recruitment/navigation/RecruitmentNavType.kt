package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentCreate : RecruitmentNavType()

    @Serializable
    data object RecruitmentApply : RecruitmentNavType()
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
    data class ApplicantManagement(val postId: Long) : RecruitmentNavType()

    @Serializable
    data class ApplicantDetail(val postId: Long, val applicantId: Long) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()
}
