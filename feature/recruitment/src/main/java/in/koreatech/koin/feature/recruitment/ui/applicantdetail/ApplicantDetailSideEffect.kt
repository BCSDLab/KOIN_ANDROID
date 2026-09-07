package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail

sealed interface ApplicantDetailSideEffect {
    data object Error : ApplicantDetailSideEffect
}
