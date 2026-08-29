package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentMain : RecruitmentNavType()

    @Serializable
    data class RecruitmentDetail(val recruitmentId: Int) : RecruitmentNavType()
}
