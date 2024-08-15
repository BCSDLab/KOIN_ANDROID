package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.article.FetchArticleUseCase
import `in`.koreatech.koin.ui.article.BoardType
import `in`.koreatech.koin.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.ui.article.state.toArticlePaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val fetchArticleUseCase: FetchArticleUseCase
) : BaseViewModel() {

    private val _articlePagination = MutableStateFlow(ArticlePaginationState(emptyList(), 0, 0, 5, 1))
    val articlePagination = _articlePagination.asStateFlow()

    private val _currentBoard = MutableStateFlow(BoardType.ALL)
    val currentBoard = _currentBoard.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    private val _pageNumbers = MutableStateFlow(Array<Int?>(5) { null })    // null일 경우 존재하지 않는 페이지
    val pageNumbers = _pageNumbers.asStateFlow()

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        viewModelScope.launch {
            currentBoard.combine(currentPage) { board, page ->
                _isLoading.value = true
                fetchArticleUseCase.fetchArticlePagination(board.id, page, PAGE_LIMIT).also {
                    _isLoading.value = false
                }
            }.collect { result ->
                result.onSuccess {
                    _articlePagination.emit(it.toArticlePaginationState())
                }.onFailure {
                    // TODO: Handle error
                }
            }
        }
    }

    fun setCurrentBoard(board: BoardType) {
        _currentBoard.value = board
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun calculatePageNumber() {
        val newPageNumbers = pageNumbers.value.copyOf()
        repeat(pageNumbers.value.size) { index ->
            val pageNumber = ((currentPage.value - 1) / 5) * 5 + index + 1
            if (pageNumber <= articlePagination.value.totalPage) {
                newPageNumbers[index] = pageNumber
            } else
                newPageNumbers[index] = null
        }
        if (pageNumbers.value.contentEquals(newPageNumbers).not()) {
            _pageNumbers.value = newPageNumbers
        }
    }

    companion object {
        private const val PAGE_LIMIT = 10
    }
}