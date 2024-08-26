package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.ui.article.BoardType
import `in`.koreatech.koin.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.ui.article.state.toArticlePaginationState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    articleRepository: ArticleRepository,
) : BaseViewModel() {

    val currentBoard = savedStateHandle.getStateFlow(BOARD_TYPE, BoardType.ALL)
    val currentPage = savedStateHandle.getStateFlow(CURRENT_PAGE, 1)
    val pageNumbers = savedStateHandle.getStateFlow(PAGE_NUMBERS, IntArray(PAGE_NUMBER_COUNT))  // 값이 0일 경우 존재하지 않는 페이지

    val articlePagination: StateFlow<ArticlePaginationState> = currentBoard.combine(currentPage) { board, page ->
        _isLoading.value = true
        articleRepository.fetchArticlePagination(board.id, page, ARTICLES_PER_PAGE)
    }.flatMapLatest {
        it.mapLatest { articlePagination ->
            articlePagination.toArticlePaginationState()
        }
    }.onEach {
        _isLoading.value = false
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArticlePaginationState(emptyList(), 0, 0, 5, 1)
    )

    fun setCurrentBoard(board: BoardType) {
        savedStateHandle[BOARD_TYPE] = board
        setCurrentPage(1)
    }

    fun setCurrentPage(page: Int) {
        savedStateHandle[CURRENT_PAGE] = page
    }

    fun calculatePageNumber() {
        val newPageNumbers = pageNumbers.value.copyOf()
        repeat(pageNumbers.value.size) { index ->
            val pageNumber = ((currentPage.value - 1) / PAGE_NUMBER_COUNT) * PAGE_NUMBER_COUNT + index + 1
            if (pageNumber <= articlePagination.value.totalPage) {
                newPageNumbers[index] = pageNumber
            } else {
                newPageNumbers[index] = 0
            }
        }

        if (pageNumbers.value.contentEquals(newPageNumbers).not())
            savedStateHandle[PAGE_NUMBERS] = newPageNumbers
    }

    companion object {
        private const val ARTICLES_PER_PAGE = 10
        private const val PAGE_NUMBER_COUNT = 5
        private const val BOARD_TYPE = "board_type"
        private const val CURRENT_PAGE = "current_page"
        private const val PAGE_NUMBERS = "page_numbers"
    }
}