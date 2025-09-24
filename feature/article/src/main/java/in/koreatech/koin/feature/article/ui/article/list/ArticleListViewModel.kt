package `in`.koreatech.koin.feature.article.ui.article.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val currentBoard = savedStateHandle.getStateFlow(BOARD_TYPE, ArticleBoardType.ALL)
    private var _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex get() = _selectedTabIndex

    fun setCurrentBoard(board: ArticleBoardType) {
        if (currentBoard.value == board) return
        savedStateHandle[BOARD_TYPE] = board
    }

    fun setTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }

    companion object {
        private const val BOARD_TYPE = "board_type"
    }
}
