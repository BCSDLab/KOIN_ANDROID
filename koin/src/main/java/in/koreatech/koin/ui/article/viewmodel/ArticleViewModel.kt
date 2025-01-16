package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.ui.article.ArticleBoardType
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val searchBoard = savedStateHandle.getStateFlow(SEARCH_BOARD, ArticleBoardType.ALL)

    fun setSearchBoard(board: ArticleBoardType) {
        if (searchBoard.value == board) return
        savedStateHandle[SEARCH_BOARD] = board
    }

    companion object {
        private const val SEARCH_BOARD = "search_board"
    }
}