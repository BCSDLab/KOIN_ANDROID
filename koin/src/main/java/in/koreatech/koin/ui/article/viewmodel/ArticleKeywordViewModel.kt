package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.article.FetchMyKeywordUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleKeywordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    fetchMyKeywordUseCase: FetchMyKeywordUseCase
) : BaseViewModel() {

    val myKeywords: StateFlow<List<String>> = fetchMyKeywordUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    private fun saveKeyword(keyword: String) {

    }

    companion object {
        const val MAX_KEYWORD_COUNT = 10
    }
}