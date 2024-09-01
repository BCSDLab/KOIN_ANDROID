package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.article.FetchSearchHistoryUseCase
import `in`.koreatech.koin.domain.usecase.article.SearchArticleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val fetchSearchHistoryUseCase: FetchSearchHistoryUseCase,
    private val searchArticleUseCase: SearchArticleUseCase
) : ViewModel() {

    val query = savedStateHandle.getStateFlow(SEARCH_INPUT, "")

    val searchHistory: StateFlow<List<String>> = fetchSearchHistoryUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _searchResultUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchResultUiState: StateFlow<SearchUiState> = _searchResultUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState.Idle
    )

    val mostSearchedKeywords: StateFlow<List<String>> = articleRepository.fetchMostSearchedKeywords(
        MOST_SEARCHED_KEYWORD_COUNT
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun onSearchInputChanged(query: String) {
        savedStateHandle[SEARCH_INPUT] = query
    }

    // TODO : UI아직 없음 + boardId, page, limit 지정
    fun search() {
        if (query.value.trim().isEmpty()) {
            _searchResultUiState.value = SearchUiState.RequireInput
            return
        }

        searchArticleUseCase(query.value, 4, 1, 10)
            .onStart {
                _searchResultUiState.value = SearchUiState.Loading
            }.onEach {
                if (it.articleHeaders.isEmpty()) {
                    _searchResultUiState.emit(SearchUiState.Empty)
                } else {
                    _searchResultUiState.emit(SearchUiState.Success(it))
                }
            }.launchIn(viewModelScope)
    }

    fun deleteSearchHistory(vararg query: String) {
        articleRepository.deleteSearchHistory(*query)
    }

    fun clearSearchHistory() {
        deleteSearchHistory(*searchHistory.value.toTypedArray())
    }

    companion object {
        private const val MOST_SEARCHED_KEYWORD_COUNT = 5
        private const val SEARCH_INPUT = "search_input"
    }
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Empty : SearchUiState
    data object Loading : SearchUiState
    data class Success(val articlePagination: ArticlePagination) : SearchUiState
    data object RequireInput : SearchUiState
}