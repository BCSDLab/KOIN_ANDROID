package `in`.koreatech.koin.feature.recruitment.ui.profile

sealed interface ProfileSideEffect {
    data object NavigateUp : ProfileSideEffect
    data object NavigateToMyRecruitment : ProfileSideEffect
    data object NavigateToMyAppliedRecruitment : ProfileSideEffect
    data class NavigateToProfileCreate(val isEditMode: Boolean) : ProfileSideEffect
}
