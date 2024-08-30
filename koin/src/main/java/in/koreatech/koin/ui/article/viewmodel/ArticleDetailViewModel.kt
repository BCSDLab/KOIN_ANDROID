package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.article.FetchHotArticlesUseCase
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.state.HtmlElement
import `in`.koreatech.koin.ui.article.state.toArticleHeaderState
import `in`.koreatech.koin.ui.article.state.toArticleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted("articleId") articleId: Int,
    @Assisted("navigatedBoardId") val navigatedBoardId: Int,
    articleRepository: ArticleRepository,
    fetchHotArticlesUseCase: FetchHotArticlesUseCase,
) : BaseViewModel() {

    val article: StateFlow<ArticleState> =
        articleRepository.fetchArticle(articleId, navigatedBoardId)
            .onStart {
                _isLoading.value = true
            }.map {
                it.toArticleState()
            }.onEach {
                _isLoading.value = false
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ArticleState(
                    header = ArticleHeaderState(
                        id = 0,
                        boardId = 0,
                        boardName = null,
                        title = "",
                        author = "",
                        viewCount = 0,
                        createdAt = "",
                        updatedAt = "",
                    ),
                    content = HtmlElement(HtmlTag.UNKNOWN),
                    prevArticleId = null,
                    nextArticleId = null
                )
            )

    val hotArticles: StateFlow<List<ArticleHeaderState>> = fetchHotArticlesUseCase()
        .map {
            var doesHotContainsThis = false
            it.filterIndexed { index, hotArticleHeader ->
                if (articleId == hotArticleHeader.id)
                    doesHotContainsThis = true
                articleId != hotArticleHeader.id && index < (HOT_ARTICLE_COUNT + if(doesHotContainsThis) 1 else 0)
            }.map { it.toArticleHeaderState() }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("articleId") articleId: Int,
            @Assisted("navigatedBoardId") navigatedBoardId: Int,
        ): ArticleDetailViewModel
    }

    companion object {
        private const val HOT_ARTICLE_COUNT = 4

        fun provideFactory(
            assistedFactory: Factory,
            article: Int,
            boardId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(article, boardId) as T
                }
            }
        }
    }
}