package `in`.koreatech.koin.feature.club.intent.detail

sealed class ClubDetailIntent {
    object LoadClubDetailsAndQnas : ClubDetailIntent()
    object LoadClubDetails : ClubDetailIntent()
    object LoadClubQnas : ClubDetailIntent()
    data class AddClubQna(val parentId: Int?, val content: String) : ClubDetailIntent()
    data class DeleteClubQna(val qnaId: Int) : ClubDetailIntent()
    object ChangeClubLike : ClubDetailIntent()
    object SetClubEmpowerment : ClubDetailIntent()
}
