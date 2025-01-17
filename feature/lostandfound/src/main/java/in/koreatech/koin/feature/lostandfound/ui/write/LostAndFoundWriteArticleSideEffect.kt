package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.net.Uri

sealed class LostAndFoundWriteArticleSideEffect {
    data class AddImage(val itemIndex: Int, val imageIndex: Int, val imageUri: Uri, val tooManyImage: Boolean) : LostAndFoundWriteArticleSideEffect()
    data object FailedToUploadImage : LostAndFoundWriteArticleSideEffect()
    data class CheckAllFieldValid(val itemList: List<LostAndFoundWriteArticleItemState>) : LostAndFoundWriteArticleSideEffect()
    data class LostAndFoundWriteArticle(val articleId: Int) : LostAndFoundWriteArticleSideEffect()
    data object LostAndFoundWriteArticleFailed : LostAndFoundWriteArticleSideEffect()
}