package `in`.koreatech.koin.feature.club.intent.detail

sealed class ClubDetailIntent {
    object loadClubDetailsAndQnas : ClubDetailIntent()
    object LoadClubDetails : ClubDetailIntent()
    object LoadClubQnas : ClubDetailIntent()
    data class addClubQna(val parentId: Int?, val content: String) : ClubDetailIntent()
    data class deleteClubQna(val qnaId: Int) : ClubDetailIntent()
    object changeClubLike : ClubDetailIntent()
    object setClubEmpowerment : ClubDetailIntent()
}
