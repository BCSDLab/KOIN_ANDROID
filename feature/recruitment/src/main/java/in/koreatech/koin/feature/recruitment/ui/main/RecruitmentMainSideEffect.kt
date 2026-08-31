package `in`.koreatech.koin.feature.recruitment.ui.main

sealed interface RecruitmentMainSideEffect {
    data object ShowError : RecruitmentMainSideEffect
}
