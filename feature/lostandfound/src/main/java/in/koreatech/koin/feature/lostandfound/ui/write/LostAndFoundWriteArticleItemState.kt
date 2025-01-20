package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

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
        category = category.getCategoryKoreanWord(),
        foundPlace = foundPlace,
        foundDate = foundDate.toString(),
        content = content,
        images = images.map { it }
    )
}