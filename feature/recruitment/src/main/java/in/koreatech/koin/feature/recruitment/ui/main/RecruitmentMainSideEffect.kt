package `in`.koreatech.koin.feature.recruitment.ui.main

sealed interface RecruitmentMainSideEffect {
    data object ShowError : RecruitmentMainSideEffect
    data object NavigateToWrite : RecruitmentMainSideEffect
    data object NavigateToLogin : RecruitmentMainSideEffect
}
