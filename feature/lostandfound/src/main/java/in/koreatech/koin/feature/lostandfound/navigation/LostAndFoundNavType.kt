package `in`.koreatech.koin.feature.lostandfound.navigation

sealed class LostAndFoundNavType(val route: String) {
    data object LostAndFoundList : LostAndFoundNavType("lost_and_found_list")
    data object LostAndFoundDetail : LostAndFoundNavType("lost_and_found_detail")
    data object LostAndFoundReport : LostAndFoundNavType("lost_and_found_report")
    data object LostAndFoundWrite : LostAndFoundNavType("lost_and_found_write")
}
