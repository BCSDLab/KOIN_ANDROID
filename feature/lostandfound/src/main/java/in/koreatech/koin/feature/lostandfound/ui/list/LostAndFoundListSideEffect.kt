package `in`.koreatech.koin.feature.lostandfound.ui.list

sealed class LostAndFoundListSideEffect {
    data object FetchData : LostAndFoundListSideEffect()
}
