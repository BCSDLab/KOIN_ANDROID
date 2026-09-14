package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate

sealed interface RecruitmentCreateSideEffect {
    data object NavigateUp : RecruitmentCreateSideEffect
    data object RecruitmentCreateSuccess : RecruitmentCreateSideEffect
    data object RecruitmentCreateFailure : RecruitmentCreateSideEffect
}
