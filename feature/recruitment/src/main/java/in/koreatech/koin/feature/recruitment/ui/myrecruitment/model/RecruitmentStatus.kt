package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

sealed class RecruitmentStatus {
    data class Recruiting(val daysLeft: Int) : RecruitmentStatus()
    data object Complete : RecruitmentStatus()
}
