package `in`.koreatech.koin.feature.article.ui.lostandfound.write

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.feature.article.enums.LostItemCategory
import `in`.koreatech.koin.feature.article.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundWriteArticleItemState(
    val lostOrFoundType: LostOrFoundType = LostOrFoundType.FOUND,
    val itemTypeRequired: Boolean = false,
    val locationRequired: Boolean = false,
    val dateRequired: Boolean = false,
    val category: LostItemCategory = LostItemCategory.NONE,
    val foundPlace: String = "",
    val foundDate: LocalDate? = null,
    val content: String? = null,
    val images: List<String> = emptyList()
) : Parcelable

fun LostAndFoundWriteArticleItemState.toArticleLostAndFoundUpload(): ArticleLostAndFoundUpload {
    return ArticleLostAndFoundUpload(
        type = lostOrFoundType.name,
        category = category.getCategoryKoreanWord(),
        foundPlace = foundPlace.trim(),
        foundDate = foundDate.toString(),
        content = content,
        images = images.map { it }
    )
}
