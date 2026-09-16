package `in`.koreatech.koin.feature.recruitment.ui.main

sealed interface RecruitmentMainSideEffect {
    data object ShowError : RecruitmentMainSideEffect
    data object ShowLoginRequiredToast : RecruitmentMainSideEffect
    data object NavigateToWrite : RecruitmentMainSideEffect
    data object NavigateToProfile : RecruitmentMainSideEffect
    data object NavigateToNotification : RecruitmentMainSideEffect
    data object NavigateToLogin : RecruitmentMainSideEffect
}
