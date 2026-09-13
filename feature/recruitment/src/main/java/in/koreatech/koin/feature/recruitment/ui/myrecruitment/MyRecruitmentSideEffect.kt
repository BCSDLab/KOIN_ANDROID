package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

sealed interface MyRecruitmentSideEffect {
    data object NavigateToLogin : MyRecruitmentSideEffect
}
