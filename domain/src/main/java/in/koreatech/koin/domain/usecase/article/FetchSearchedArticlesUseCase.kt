package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSearchedArticlesUseCase
    @Inject
    constructor(
        private val articleRepository: ArticleRepository,
    ) {
        operator fun invoke(
            query: String,
            boardId: Int,
            page: Int,
            limit: Int,
        ): Flow<ArticlePagination> = articleRepository.fetchSearchedArticles(query, boardId, page, limit)
    }
