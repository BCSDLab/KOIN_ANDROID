package `in`.koreatech.koin.feature.club.ui.clubrecruitmodify

sealed class ClubRecruitModifySideEffect {
    data object RecruitModifySuccess : ClubRecruitModifySideEffect()
    data object RecruitModifyFailure : ClubRecruitModifySideEffect()
    data object ClubImageUploadFailure : ClubRecruitModifySideEffect()
    data object LoadClubRecruitmentError : ClubRecruitModifySideEffect()
    data object NavigateUp : ClubRecruitModifySideEffect()
}