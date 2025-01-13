package `in`.koreatech.koin.feature.lostandfound.ui.detail

sealed class LostAndFoundDetailSideEffect {
    data class FetchDetail(val id: Int) : LostAndFoundDetailSideEffect()
    data object FetchHotArticles : LostAndFoundDetailSideEffect()
    data class DeleteArticle(val id: Int) : LostAndFoundDetailSideEffect()
    data object DeleteArticleFailed : LostAndFoundDetailSideEffect()
}