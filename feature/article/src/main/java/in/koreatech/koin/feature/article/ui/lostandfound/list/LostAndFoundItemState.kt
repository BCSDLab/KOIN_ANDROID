package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader
import `in`.koreatech.koin.feature.article.enums.LostItemCategory
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundItemState(
    val id: Int,
    val boardId: Int,
    val lostOrFound: LostOrFoundType,
    val category: LostItemCategory,
    val foundPlace: String,
    val foundDate: LocalDate,
    val content: String,
    val author: String,
    val isReported: Boolean,
    val registeredAt: LocalDate,
    val updatedAt: String
) : Parcelable

fun ArticleLostAndFoundHeader.toLostAndFoundItemState() =
    LostAndFoundItemState(
        id = id,
        boardId = boardId,
        lostOrFound = LostOrFoundType.entries.find { it.name == type } ?: LostOrFoundType.FOUND, // Hardcode value to FOUND for now
        category = LostItemCategory.safeValueOf(category),
        foundPlace = foundPlace,
        foundDate = LocalDate.parse(foundDate),
        content = content ?: "",
        author = author,
        isReported = isReported,
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt
    )

fun ArticleHeader.toLostAndFoundItemState(): LostAndFoundItemState {
    val title = title.split("|") // Backend saves title as "category|foundPlace|foundDate"
    val titleDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")

    return LostAndFoundItemState(
        id = id,
        boardId = boardId,
        lostOrFound = LostOrFoundType.FOUND, // Hardcode value to FOUND for now
        category = LostItemCategory.safeValueOf(title[0]),
        foundPlace = title[1].trim(),
        foundDate = LocalDate.parse(title[2].trim(), titleDateFormatter),
        content = "",
        author = author,
        isReported = false, // Set default value to false because search api doesn't have this field
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt
    )
}
