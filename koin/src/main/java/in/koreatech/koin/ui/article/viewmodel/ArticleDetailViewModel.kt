package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.domain.usecase.article.FetchArticleUseCase
import `in`.koreatech.koin.domain.usecase.article.FetchHotArticlesUseCase
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.HtmlElement
import `in`.koreatech.koin.ui.article.state.toArticleHeaderState
import `in`.koreatech.koin.ui.article.state.toHtmlElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted val articleHeader: ArticleHeaderState,
    private val fetchArticleUseCase: FetchArticleUseCase,
    fetchHotArticlesUseCase: FetchHotArticlesUseCase,
) : BaseViewModel() {

    private val _htmlContent = MutableStateFlow(HtmlElement(HtmlTag.UNKNOWN))
    val htmlContent = _htmlContent.asStateFlow()

    val hotArticles: StateFlow<List<ArticleHeaderState>> = fetchHotArticlesUseCase()
        .map {
            var doesHotContainsThis = false
            it.filterIndexed { index, hotArticleHeader ->
                if (articleHeader.id == hotArticleHeader.id)
                    doesHotContainsThis = true
                articleHeader.id != hotArticleHeader.id && index < (HOT_ARTICLE_COUNT + if(doesHotContainsThis) 1 else 0)
            }.map { it.toArticleHeaderState() }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        fetchHtmlContent()
    }

    private fun fetchHtmlContent() {
        viewModelScope.launchWithLoading {
            fetchArticleUseCase.fetchArticle(articleHeader.id)
                .onSuccess {
                    _htmlContent.value = it.html.toHtmlElement()
                }.onFailure {
                    // Handle error
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(articleHeader: ArticleHeaderState): ArticleDetailViewModel
    }

    companion object {
        private const val HOT_ARTICLE_COUNT = 4
        fun provideFactory(
            assistedFactory: Factory,
            article: ArticleHeaderState
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(article) as T
                }
            }
        }
    }
}