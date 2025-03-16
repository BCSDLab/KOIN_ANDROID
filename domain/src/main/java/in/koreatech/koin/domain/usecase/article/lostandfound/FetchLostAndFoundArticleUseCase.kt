package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchLostAndFoundArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(articleId: Int): Flow<ArticleLostAndFound> = articleRepository.fetchArticleLostAndFound(articleId)
}
