package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchLostAndFoundArticlePaginationUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int,
        type: String?
    ): Flow<ArticleLostAndFoundPagination> = articleRepository.fetchArticleLostAndFoundPagination(page, limit, type)
}
