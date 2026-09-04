package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

sealed interface RecruitmentModifySideEffect {
    data object NavigateUp : RecruitmentModifySideEffect
    data object RecruitmentModifySuccess : RecruitmentModifySideEffect
    data object RecruitmentModifyFailure : RecruitmentModifySideEffect
}
