package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchArticleV2UseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(articleId: Int, boardId: Int): Flow<Article> =
        articleRepository.fetchArticleV2(articleId, boardId)
}
