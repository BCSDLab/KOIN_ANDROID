package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchSearchedLostAndFoundArticlesUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(
        query: String,
        page: Int,
        limit: Int
    ): Flow<ArticleLostAndFoundPagination> = articleRepository.fetchSearchedLostAndFoundArticles(query, page, limit)
}
