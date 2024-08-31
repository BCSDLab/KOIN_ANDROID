package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SearchArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {

    operator fun invoke(query: String, boardId: Int, page: Int, limit: Int): Flow<ArticlePagination> {
        return articleRepository.fetchSearchedArticles(query, boardId, page, limit)
            .onStart { articleRepository.saveSearchHistory(query) }
    }
}