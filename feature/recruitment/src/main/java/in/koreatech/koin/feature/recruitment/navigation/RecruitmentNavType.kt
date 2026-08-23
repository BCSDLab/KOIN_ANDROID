package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object MyRecruitmentList : RecruitmentNavType()

    @Serializable
    data object MyAppliedRecruitmentList : RecruitmentNavType()
}
