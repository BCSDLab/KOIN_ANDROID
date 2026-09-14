package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

sealed class AppliedRecruitmentStatus {
    data object Approved : AppliedRecruitmentStatus()
    data object Pending : AppliedRecruitmentStatus()
    data object Rejected : AppliedRecruitmentStatus()
}
