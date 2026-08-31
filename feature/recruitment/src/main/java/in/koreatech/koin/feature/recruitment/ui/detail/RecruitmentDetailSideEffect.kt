package `in`.koreatech.koin.feature.recruitment.ui.detail

sealed interface RecruitmentDetailSideEffect {
    data object ShowError : RecruitmentDetailSideEffect

    data object DeleteSuccess : RecruitmentDetailSideEffect
}
