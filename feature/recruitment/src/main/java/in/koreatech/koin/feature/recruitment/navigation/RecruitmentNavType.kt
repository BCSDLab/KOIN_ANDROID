package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object Notification : RecruitmentNavType()

    @Serializable
    data class ApplicantManagement(val postId: Long) : RecruitmentNavType()

    @Serializable
    data class ApplicantDetail(val postId: Long, val applicantId: Long) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()
}
