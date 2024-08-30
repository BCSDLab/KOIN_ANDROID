package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    articleRepository: ArticleRepository,
) : ViewModel() {

    val mostSearchedKeywords: StateFlow<List<String>> = articleRepository.fetchMostSearchedKeywords(
        MOST_SEARCHED_KEYWORD_COUNT
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    companion object {
        private const val MOST_SEARCHED_KEYWORD_COUNT = 5
    }
}