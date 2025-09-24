package `in`.koreatech.koin.feature.article.ui.lostandfound.list

sealed class LostAndFoundSideEffect {
    data class PageChanged(val page: Int) : LostAndFoundSideEffect()
    data object KeywordUpdated : LostAndFoundSideEffect() // Called if keyword updated
}
