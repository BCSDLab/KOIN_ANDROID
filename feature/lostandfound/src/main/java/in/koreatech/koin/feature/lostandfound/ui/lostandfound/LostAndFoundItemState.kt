package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

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
    val registeredAt: LocalDate,
    val updatedAt: String,
) : Parcelable

fun ArticleLostAndFoundHeader.toLostAndFoundItemState() = LostAndFoundItemState(
    id = id,
    boardId = boardId,
    lostOrFound = LostOrFoundType.FOUND, // Hardcode value to FOUND for now
    category = LostItemCategory.safeValueOf(category),
    foundPlace = foundPlace,
    foundDate = LocalDate.parse(foundDate),
    content = content ?: "",
    author = author,
    registeredAt = LocalDate.parse(registeredAt),
    updatedAt = updatedAt
)

fun ArticleHeader.toLostAndFoundItemState(): LostAndFoundItemState {
    val title = title.split("|") // Backend saves title as "category|foundPlace|foundDate"

    return LostAndFoundItemState(
        id = id,
        boardId = boardId,
        lostOrFound = LostOrFoundType.FOUND, // Hardcode value to FOUND for now
        category = LostItemCategory.safeValueOf(title[0]),
        foundPlace = title[1],
        foundDate = LocalDate.parse(title[2]),
        content = "",
        author = author,
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt
    )
}