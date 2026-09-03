package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment

sealed interface MyAppliedRecruitmentSideEffect {
    data object NavigateToLogin : MyAppliedRecruitmentSideEffect
}
