package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

sealed interface RecruitmentApplySideEffect {
    data object NavigateUp : RecruitmentApplySideEffect
    data object ApplySuccess : RecruitmentApplySideEffect
    data object ApplyFailure : RecruitmentApplySideEffect
}
