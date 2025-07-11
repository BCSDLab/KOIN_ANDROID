package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

sealed class ClubRecruitCreateSideEffect {
    data object RecruitCreateSuccess : ClubRecruitCreateSideEffect()
    data object RecruitCreateFailure : ClubRecruitCreateSideEffect()
    data object ClubImageUploadFailure : ClubRecruitCreateSideEffect()
}
