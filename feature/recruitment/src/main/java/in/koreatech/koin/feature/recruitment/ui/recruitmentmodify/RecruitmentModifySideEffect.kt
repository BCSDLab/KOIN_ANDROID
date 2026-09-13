package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

sealed interface RecruitmentModifySideEffect {
    data object NavigateUp : RecruitmentModifySideEffect
    data object RecruitmentModifySuccess : RecruitmentModifySideEffect
    data class RecruitmentModifyFailure(val message: String?) : RecruitmentModifySideEffect
    data class ShowLoadError(val message: String?) : RecruitmentModifySideEffect
}
