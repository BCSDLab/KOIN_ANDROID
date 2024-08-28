package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleKeywordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository
) : BaseViewModel() {

    val keywordInputUiState: StateFlow<KeywordInputUiState> = savedStateHandle.getStateFlow(KEYWORD_INPUT, "").map {
        if (it.isEmpty()) KeywordInputUiState.Empty else KeywordInputUiState.Valid(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = KeywordInputUiState.Empty
    )

    val myKeywords: StateFlow<List<String>> = articleRepository.fetchMyKeyword()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    private val _keywordAddUiState = MutableStateFlow<KeywordAddUiState>(KeywordAddUiState.Nothing)
    val keywordAddUiState: StateFlow<KeywordAddUiState> = _keywordAddUiState

    val suggestedKeywords: StateFlow<List<String>> = articleRepository.fetchKeywordSuggestions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun addKeyword(keyword: String) {
        val trimmedKeyword = keyword.trim().ifEmpty {
            _keywordAddUiState.value = KeywordAddUiState.RequireInput
            return
        }
        if (myKeywords.value.size >= MAX_KEYWORD_COUNT) {
            _keywordAddUiState.value = KeywordAddUiState.LimitExceeded
            return
        }

        if (myKeywords.value.contains(trimmedKeyword)) {
            _keywordAddUiState.value = KeywordAddUiState.AlreadyExist
            return
        }

        if (trimmedKeyword.contains(" ")) {
            _keywordAddUiState.value = KeywordAddUiState.BlankNotAllowed
            return
        }

        articleRepository.saveKeyword(trimmedKeyword).onStart {
            _keywordAddUiState.emit(KeywordAddUiState.Loading)
        }.onEach {
            _keywordAddUiState.emit(KeywordAddUiState.Success)
        }.catch {
            _keywordAddUiState.emit(KeywordAddUiState.Error)
        }
    }

    fun removeKeyword(keyword: String) {

    }

    fun onKeywordInputChanged(keyword: String) {
        savedStateHandle[KEYWORD_INPUT] = keyword
    }

    companion object {
        const val MAX_KEYWORD_COUNT = 10
        const val KEYWORD_INPUT = "keyword_input"
    }
}

sealed interface KeywordAddUiState {
    data object Nothing : KeywordAddUiState
    data object Loading : KeywordAddUiState
    data object Success : KeywordAddUiState
    data object AlreadyExist : KeywordAddUiState
    data object LimitExceeded : KeywordAddUiState
    data object BlankNotAllowed : KeywordAddUiState
    data object RequireInput : KeywordAddUiState
    data object Error : KeywordAddUiState
}

sealed interface KeywordInputUiState {
    data object Empty : KeywordInputUiState
    data class Valid(val keyword: String) : KeywordInputUiState
}