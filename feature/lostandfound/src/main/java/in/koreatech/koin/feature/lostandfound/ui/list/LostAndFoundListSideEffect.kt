package `in`.koreatech.koin.feature.lostandfound.ui.list

sealed class LostAndFoundListSideEffect {
    data object FetchData : LostAndFoundListSideEffect()
    data class UpdateSignInDialog(val visible: Boolean) : LostAndFoundListSideEffect()
}
