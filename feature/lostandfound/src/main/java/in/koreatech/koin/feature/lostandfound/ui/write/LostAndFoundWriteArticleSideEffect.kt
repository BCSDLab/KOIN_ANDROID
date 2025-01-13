package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.net.Uri
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import java.time.LocalDate

sealed class LostAndFoundWriteArticleSideEffect {
    data class AddItem(val item: LostAndFoundWriteArticleItemState) : LostAndFoundWriteArticleSideEffect()
    data class RemoveItem(val index: Int) : LostAndFoundWriteArticleSideEffect()
    data class UpdateItemType(val index: Int, val itemType: LostItemCategory) : LostAndFoundWriteArticleSideEffect()
    data class AddImage(val itemIndex: Int, val imageUri: Uri, val tooManyImage: Boolean) : LostAndFoundWriteArticleSideEffect()
    data class RemoveImage(val itemIndex: Int, val imageIndex: Int) : LostAndFoundWriteArticleSideEffect()
    data object FailedToUploadImage : LostAndFoundWriteArticleSideEffect()
    data class UpdateDescription(val itemIndex: Int, val description: String) : LostAndFoundWriteArticleSideEffect()
    data class UpdateLocation(val itemIndex: Int, val location: String) : LostAndFoundWriteArticleSideEffect()
    data class UpdateDate(val itemIndex: Int, val date: LocalDate?) : LostAndFoundWriteArticleSideEffect()
    data class CheckAllFieldValid(val itemList: List<LostAndFoundWriteArticleItemState>) : LostAndFoundWriteArticleSideEffect()
    data class LostAndFoundWriteArticle(val articleId: Int) : LostAndFoundWriteArticleSideEffect()
    data object LostAndFoundWriteArticleFailed : LostAndFoundWriteArticleSideEffect()
}