package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement

sealed interface ApplicantManagementSideEffect {
    data object Error : ApplicantManagementSideEffect
}
