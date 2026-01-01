package `in`.koreatech.koin.feature.article.ui.article.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.LostAndFoundItemState
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.toLostAndFoundItemState

@Immutable
data class LostAndFoundPaginationState(
    val articleLostAndFoundHeader: List<LostAndFoundItemState>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)

fun ArticleLostAndFoundPagination.toLostAndFoundPaginationState() = LostAndFoundPaginationState(
    articleLostAndFoundHeader = articleLostAndFoundHeader.map { it.toLostAndFoundItemState() },
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)
