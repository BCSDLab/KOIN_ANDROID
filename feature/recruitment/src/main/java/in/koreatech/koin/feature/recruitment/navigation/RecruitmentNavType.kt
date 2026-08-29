package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data class ApplicantManagement(val postId: Int) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()
}
