package `in`.koreatech.koin.feature.lostandfound.ui.detail

sealed class LostAndFoundDetailSideEffect {
    data class DeleteArticle(val id: Int) : LostAndFoundDetailSideEffect()
    data object DeleteArticleFailed : LostAndFoundDetailSideEffect()
}