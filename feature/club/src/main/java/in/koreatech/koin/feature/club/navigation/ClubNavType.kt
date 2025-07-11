package `in`.koreatech.koin.feature.club.navigation

sealed class ClubNavType(val route: String) {
    data object ClubList : ClubNavType("club_list")
    data object ClubDetail : ClubNavType("club_detail")
    data object ClubCreate : ClubNavType("club_create")
    data object ClubModify : ClubNavType("club_modify")
    data object ClubRecruitCreate : ClubNavType("club_recruit_create")
    data object ClubRecruitModify : ClubNavType("club_recruit_modify")
}
