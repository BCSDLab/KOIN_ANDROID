package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchHotArticlesUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(): Flow<List<ArticleHeader>> = articleRepository.fetchHotArticleHeaders()
}
