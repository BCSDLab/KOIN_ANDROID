package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentCreate : RecruitmentNavType()
    @Serializable
    data object RecruitmentApply : RecruitmentNavType()
}
