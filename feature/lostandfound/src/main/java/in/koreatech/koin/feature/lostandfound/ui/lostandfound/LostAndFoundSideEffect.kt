package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

sealed class LostAndFoundSideEffect {
    data class PageChanged(val page: Int) : LostAndFoundSideEffect()
}
