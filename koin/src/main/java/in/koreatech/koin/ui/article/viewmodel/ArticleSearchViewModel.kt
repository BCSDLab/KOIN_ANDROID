package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.article.FetchSearchHistoryUseCase
import `in`.koreatech.koin.domain.usecase.article.SearchArticleUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
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
        // TODO : UI아직 없음 + boardId, page, limit 지정
        searchArticleUseCase(query.value, 4, 1, 10)
            .launchIn(viewModelScope)
    }

    fun deleteSearchHistory(vararg query: String) {
        articleRepository.deleteSearchHistory(*query)
    }

    fun clearSearchHistory() {
        deleteSearchHistory(*searchHistory.value.toTypedArray())
    }

    companion object {
        private const val MOST_SEARCHED_KEYWORD_COUNT = 5
        private const val MAX_SEARCH_HISTORY_COUNT = FetchSearchHistoryUseCase.MAX_SEARCH_HISTORY_COUNT
        private const val SEARCH_INPUT = "search_input"
    }
}