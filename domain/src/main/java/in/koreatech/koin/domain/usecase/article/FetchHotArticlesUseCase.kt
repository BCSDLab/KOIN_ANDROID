package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FetchHotArticlesUseCase @Inject constructor(
    articleRepository: ArticleRepository,
    dispatcher: CoroutineDispatcher
) {
    private val headers: StateFlow<List<ArticleHeader>> =
        articleRepository.fetchHotArticleHeaders().stateIn(
            scope = CoroutineScope(dispatcher),
            started = SharingStarted.WhileSubscribed(10000),
            initialValue = listOf()
        )

    operator fun invoke(): Flow<List<ArticleHeader>> {
        return headers
    }
}