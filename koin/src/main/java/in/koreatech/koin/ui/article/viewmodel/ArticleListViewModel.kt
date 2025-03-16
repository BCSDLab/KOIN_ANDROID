package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.ui.article.ArticleBoardType
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val currentBoard = savedStateHandle.getStateFlow(BOARD_TYPE, ArticleBoardType.ALL)

    fun setCurrentBoard(board: ArticleBoardType) {
        if (currentBoard.value == board) return
        savedStateHandle[BOARD_TYPE] = board
    }

    companion object {
        private const val BOARD_TYPE = "board_type"
    }
}
