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
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.HtmlElement
import `in`.koreatech.koin.ui.article.state.toHtmlElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted val articleHeader: ArticleHeaderState,
    private val fetchArticleUseCase: FetchArticleUseCase
) : BaseViewModel() {

    private val _htmlContent = MutableStateFlow(HtmlElement(HtmlTag.UNKNOWN))
    val htmlContent = _htmlContent.asStateFlow()

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
        fun create(articleId: ArticleHeaderState): ArticleDetailViewModel
    }

    companion object {
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