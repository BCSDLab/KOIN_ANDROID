package `in`.koreatech.koin.feature.article.ui.lostandfound.detail

sealed class LostAndFoundDetailSideEffect {
    // data class FetchDetail(val id: Int) : LostAndFoundDetailSideEffect()
    // data object FetchHotArticles : LostAndFoundDetailSideEffect()
    data class DeleteArticle(val id: Int) : LostAndFoundDetailSideEffect()

    data object DeleteArticleFailed : LostAndFoundDetailSideEffect()

    data object DeletedArticle : LostAndFoundDetailSideEffect()
}
