package `in`.koreatech.koin.ui.article.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.ui.article.BoardType
import `in`.koreatech.koin.ui.article.state.ArticleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(

) : BaseViewModel() {

    private val articleMap = mutableMapOf<BoardType, List<ArticleState>>()

    private val _articles = MutableStateFlow<List<ArticleState>>(emptyList())
    val articles = _articles.asStateFlow()

    private val _currentBoard = MutableStateFlow(BoardType.ALL)
    val currentBoard = _currentBoard.asStateFlow()

    fun fetchArticles(board: BoardType) {
        if (articleMap[board].isNullOrEmpty()) {
            // fetch articles from server
            // articleMap[board] = fetchedArticles
            _articles.value = articleMap[board] ?: emptyList()
        }
    }

    fun setCurrentBoard(board: BoardType) {
        _currentBoard.value = board
    }
}