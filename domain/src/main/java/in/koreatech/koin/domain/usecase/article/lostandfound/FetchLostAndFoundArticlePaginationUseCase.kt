package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchLostAndFoundArticlePaginationUseCase
    @Inject
    constructor(
        private val articleRepository: ArticleRepository,
    ) {
        operator fun invoke(
            page: Int,
            limit: Int,
            type: String?,
        ): Flow<ArticleLostAndFoundPagination> = articleRepository.fetchArticleLostAndFoundPagination(page, limit, type)
    }
