package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader
import `in`.koreatech.koin.feature.article.enums.LostItemCategory
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import java.time.LocalDate
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
