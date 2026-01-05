package `in`.koreatech.koin.feature.article.ui.lostandfound.detail

import android.net.Uri
import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.feature.article.enums.LostItemCategory
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundDetailState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentLoggedInUser: String = "",
    val showDeleteDialog: Boolean = false,
    val lostOrFound: LostOrFoundType = LostOrFoundType.FOUND,
    val id: Int = 0,
    val category: LostItemCategory = LostItemCategory.NONE,
    val foundPlace: String = "",
    val foundDate: LocalDate = LocalDate.MIN,
    val content: String = "",
    val author: String = "",
    val images: List<Uri>? = null,
    val registeredAt: LocalDate = LocalDate.MIN,
    val updatedAt: String = "",
    val isWriterCouncil: Boolean = false,
    val isMine: Boolean = false,
    val isAuthorWithdraw: Boolean = false,
    val hotArticles: List<ArticleHeaderState> = emptyList()
) : Parcelable

fun ArticleLostAndFound.toLostAndFoundDetailState(): LostAndFoundDetailState {
    return LostAndFoundDetailState(
        lostOrFound = LostOrFoundType.entries.find { it.name == type } ?: LostOrFoundType.FOUND,
        id = id,
        category = LostItemCategory.safeValueOf(category),
        foundPlace = foundPlace,
        foundDate = LocalDate.parse(foundDate),
        content = content ?: "",
        author = author,
        images = images?.map { Uri.parse(it.imageUrl) },
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt,
        isWriterCouncil = isCouncil,
        isMine = isMine
    )
}
