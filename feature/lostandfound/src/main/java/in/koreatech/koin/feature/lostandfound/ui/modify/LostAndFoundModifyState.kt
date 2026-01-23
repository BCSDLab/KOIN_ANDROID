package `in`.koreatech.koin.feature.lostandfound.ui.modify

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundModifyState(
    val isLoading: Boolean = false,
    val articleId: Int = -1,
    val lostOrFoundType: LostOrFoundType = LostOrFoundType.FOUND,
    val itemTypeRequired: Boolean = false,
    val locationRequired: Boolean = false,
    val category: LostItemCategory = LostItemCategory.NONE,
    val foundPlace: String = "",
    val foundDate: LocalDate = LocalDate.now(),
    val refreshDatePicker: Boolean = false,
    val content: String? = null,
    val images: ImmutableList<String> = persistentListOf()
) : Parcelable

fun ArticleLostAndFound.toLostAndFoundModifyState(): LostAndFoundModifyState {
    return LostAndFoundModifyState(
        lostOrFoundType = LostOrFoundType.entries.find { it.name == type } ?: LostOrFoundType.FOUND,
        articleId = id,
        category = LostItemCategory.safeValueOf(category),
        foundPlace = foundPlace,
        foundDate = LocalDate.parse(foundDate),
        content = content ?: "",
        images = images?.map { it.imageUrl }?.toPersistentList() ?: persistentListOf()
    )
}
