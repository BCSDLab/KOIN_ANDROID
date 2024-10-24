package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.ui.article.ArticleBoardType
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.state.toArticleHeaderState
import `in`.koreatech.koin.ui.article.state.toArticleState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted("articleId") articleId: Int,
    @Assisted("navigatedBoardId") val navigatedBoardId: Int,
    private val articleRepository: ArticleRepository,
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
                        board = ArticleBoardType.ALL,
                        title = "",
                        author = "",
                        viewCount = 0,
                        registeredAt = "",
                        updatedAt = "",
                    ),
                    content = "",
                    prevArticleId = null,
                    nextArticleId = null,
                    attachments = listOf()
                )
            )

    val hotArticles: StateFlow<List<ArticleHeaderState>> = articleRepository.fetchHotArticleHeaders()
        .map {
            var doesHotContainsThis = false
            it.filterIndexed { index, hotArticleHeader ->
                if (articleId == hotArticleHeader.id)
                    doesHotContainsThis = true
                articleId != hotArticleHeader.id && index < (HOT_ARTICLE_COUNT + if (doesHotContainsThis) 1 else 0)
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
        const val HOT_ARTICLE_COUNT = 4

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