package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class UpdateItemFoundUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(
        articleId: Int
    ): Result<Unit> {
        return articleRepository.updateItemFound(articleId)
    }
}