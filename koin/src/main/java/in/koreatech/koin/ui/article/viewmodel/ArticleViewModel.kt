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
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val fetchArticleUseCase: FetchArticleUseCase
) : BaseViewModel() {

    private val _articlePage = MutableStateFlow(ArticlePaginationState(emptyList(), 0, 0, 1, 1))
    val articlePage = _articlePage.asStateFlow()

    private val _currentBoard = MutableStateFlow(BoardType.ALL)
    val currentBoard = _currentBoard.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    fun fetchArticles(board: BoardType, page: Int) {
        _currentPage.value = page
        viewModelScope.launchWithLoading {
            fetchArticleUseCase.fetchArticlePagination(board.id, page, PAGE_LIMIT).onSuccess {
                _articlePage.emit(it.toArticlePaginationState())
            }.onFailure {
                TODO("Handle error")
            }
        }
    }

    fun setCurrentBoard(board: BoardType) {
        _currentBoard.value = board
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    companion object {
        private const val PAGE_LIMIT = 10
    }
}