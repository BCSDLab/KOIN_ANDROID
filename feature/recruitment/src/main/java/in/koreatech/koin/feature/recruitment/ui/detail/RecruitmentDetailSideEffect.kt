package `in`.koreatech.koin.feature.recruitment.ui.detail

sealed interface RecruitmentDetailSideEffect {
    data object ShowLoadError : RecruitmentDetailSideEffect

    data object ShowDeleteError : RecruitmentDetailSideEffect

    data object DeleteSuccess : RecruitmentDetailSideEffect
}
