package `in`.koreatech.koin.feature.lostandfound.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
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
    val isFound: Boolean,
    val registeredAt: LocalDate,
    val updatedAt: String
) : Parcelable

fun ArticleLostAndFoundHeader.toLostAndFoundItemState() =
    LostAndFoundItemState(
        id = id,
        boardId = boardId,
        lostOrFound = LostOrFoundType.entries.find { it.name == type } ?: LostOrFoundType.FOUND,
        category = LostItemCategory.safeValueOf(category),
        foundPlace = foundPlace,
        foundDate = LocalDate.parse(foundDate),
        content = content ?: "",
        author = author,
        isReported = isReported,
        isFound = isFound,
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt
    )
