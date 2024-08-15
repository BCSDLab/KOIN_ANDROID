package `in`.koreatech.koin.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.ui.article.state.ArticleState

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted val article: ArticleState
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(article: ArticleState): ArticleDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            article: ArticleState
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(article) as T
                }
            }
        }
    }
}