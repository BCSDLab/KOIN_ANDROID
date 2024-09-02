package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.article.FetchSearchHistoryUseCase
import `in`.koreatech.koin.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.ui.article.state.toArticlePaginationState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val fetchSearchHistoryUseCase: FetchSearchHistoryUseCase
) : ViewModel() {

    val query = savedStateHandle.getStateFlow(SEARCH_INPUT, "")

    val searchHistory: StateFlow<List<String>> = fetchSearchHistoryUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _searchResultUiState = MutableSharedFlow<SearchUiState>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val searchResultUiState: SharedFlow<SearchUiState> = _searchResultUiState.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
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

    fun search() {
        if (query.value.trim().isEmpty()) {
            _searchResultUiState.tryEmit(SearchUiState.RequireInput)
            return
        }

        articleRepository.fetchSearchedArticles(query.value, 4, 1, 10)
            .onStart {
                _searchResultUiState.tryEmit(SearchUiState.Loading)
            }.onEach {
                if (it.articleHeaders.isEmpty()) {
                    _searchResultUiState.emit(SearchUiState.Empty)
                } else {
                    _searchResultUiState.emit(SearchUiState.Success(it.toArticlePaginationState()))
                }
                articleRepository.saveSearchHistory(query.value).launchIn(viewModelScope)
            }.launchIn(viewModelScope)
    }

    fun deleteSearchHistory(vararg query: String) {
        articleRepository.deleteSearchHistory(*query).launchIn(viewModelScope)
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
    data class Success(val articlePagination: ArticlePaginationState) : SearchUiState
    data object RequireInput : SearchUiState
}