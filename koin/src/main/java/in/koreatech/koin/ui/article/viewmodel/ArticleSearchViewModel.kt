package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.ui.article.state.toArticlePaginationState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository
) : BaseViewModel() {

    val query = savedStateHandle.getStateFlow(SEARCH_INPUT, "")

    val searchHistory: StateFlow<List<String>> = articleRepository.fetchSearchHistory()
        .map {
            it.take(MAX_SEARCH_HISTORY_COUNT)
        }.stateIn(
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
        _isLoading.value = true
        val trimmedQuery = query.value.trim()
        if (trimmedQuery.isEmpty()) {
            _searchResultUiState.tryEmit(SearchUiState.RequireInput)
            return
        }

        articleRepository.fetchSearchedArticles(trimmedQuery, 4, 1, 20)
            .onStart {
                _searchResultUiState.tryEmit(SearchUiState.Loading)
            }.onEach {
                if (it.articleHeaders.isEmpty()) {
                    _searchResultUiState.emit(SearchUiState.Empty)
                } else {
                    _searchResultUiState.emit(SearchUiState.Success(it.toArticlePaginationState()))
                }
                articleRepository.saveSearchHistory(trimmedQuery).launchIn(viewModelScope)
            }.catch {
                _searchResultUiState.tryEmit(SearchUiState.Error)
            }.onCompletion { e ->
                _isLoading.value = false
            }.launchIn(viewModelScope)
    }

    fun deleteSearchHistory(query: String) {
        articleRepository.deleteSearchHistory(query).launchIn(viewModelScope)
    }

    fun clearSearchHistory() {
        articleRepository.clearSearchHistory().launchIn(viewModelScope)
    }

    companion object {
        const val MAX_SEARCH_HISTORY_COUNT = 5
        private const val MOST_SEARCHED_KEYWORD_COUNT = 5
        private const val SEARCH_INPUT = "search_input"
    }
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Empty : SearchUiState
    data object Loading : SearchUiState
    data object Error : SearchUiState
    data class Success(val articlePagination: ArticlePaginationState) : SearchUiState
    data object RequireInput : SearchUiState
}