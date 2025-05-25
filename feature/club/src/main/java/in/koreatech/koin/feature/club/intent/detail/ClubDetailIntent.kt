package `in`.koreatech.koin.feature.club.intent

sealed class ClubDetailIntent {
    object LoadClubDetails : ClubDetailIntent()
    object LoadClubQnas : ClubDetailIntent()
    object addClubQna : ClubDetailIntent()
}
