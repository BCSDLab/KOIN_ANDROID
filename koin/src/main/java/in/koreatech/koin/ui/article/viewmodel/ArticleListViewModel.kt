package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.OnBoardingRepository
import `in`.koreatech.koin.ui.article.ArticleBoardType
import `in`.koreatech.koin.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.ui.article.state.toArticlePaginationState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    articleRepository: ArticleRepository,
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel() {

    val currentBoard = savedStateHandle.getStateFlow(BOARD_TYPE, ArticleBoardType.ALL)
    val currentPage = savedStateHandle.getStateFlow(CURRENT_PAGE, 1)
    val selectedKeyword = savedStateHandle.getStateFlow(SELECTED_KEYWORD, "")

    val pageNumbers = savedStateHandle.getStateFlow(PAGE_NUMBERS, IntArray(PAGE_NUMBER_COUNT))  // 값이 0일 경우 존재하지 않는 페이지

    val myKeywords: StateFlow<List<String>> = articleRepository.fetchMyKeyword()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    val articlePagination: StateFlow<ArticlePaginationState> = combine(currentBoard, currentPage, selectedKeyword) { board, page, query ->
        _isLoading.value = true
        if (query.isEmpty())
            articleRepository.fetchArticlePagination(board.id, page, ARTICLES_PER_PAGE)
        else
            articleRepository.fetchSearchedArticles(query, board.id, page, ARTICLES_PER_PAGE)
    }.debounce(10).flatMapLatest {
        it.mapLatest { articlePagination ->
            articlePagination.toArticlePaginationState()
        }
    }.onEach {
        _isLoading.value = false
        calculatePageNumber(it.totalPage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArticlePaginationState(emptyList(), 0, 0, 5, 1)
    )

    val shouldShowKeywordTooltip: StateFlow<Boolean> = onBoardingRepository.getShouldShowKeywordTooltip()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setCurrentBoard(board: ArticleBoardType) {
        if (currentBoard.value == board) return
        savedStateHandle[BOARD_TYPE] = board
        setCurrentPage(1)
    }

    fun setCurrentPage(page: Int) {
        savedStateHandle[CURRENT_PAGE] = page
    }

    fun selectKeyword(keyword: String) {
        if (selectedKeyword.value == keyword) return
        savedStateHandle[SELECTED_KEYWORD] = keyword
        setCurrentPage(1)
    }

    private fun calculatePageNumber(totalPage: Int) {
        val newPageNumbers = pageNumbers.value.copyOf()
        repeat(PAGE_NUMBER_COUNT) { index ->
            val pageNumber = ((currentPage.value - 1) / PAGE_NUMBER_COUNT) * PAGE_NUMBER_COUNT + index + 1
            if (pageNumber <= totalPage) {
                newPageNumbers[index] = pageNumber
            } else {
                newPageNumbers[index] = 0
            }
        }

        if (pageNumbers.value.contentEquals(newPageNumbers).not())
            savedStateHandle[PAGE_NUMBERS] = newPageNumbers
    }

    fun updateShouldShowKeywordTooltip(shouldShow: Boolean) {
        onBoardingRepository.updateShouldShowKeywordTooltip(shouldShow).launchIn(viewModelScope)
    }

    companion object {
        private const val ARTICLES_PER_PAGE = 10
        private const val PAGE_NUMBER_COUNT = 5
        private const val BOARD_TYPE = "board_type"
        private const val CURRENT_PAGE = "current_page"
        private const val PAGE_NUMBERS = "page_numbers"
        private const val SELECTED_KEYWORD = "selected_keyword"
    }
}