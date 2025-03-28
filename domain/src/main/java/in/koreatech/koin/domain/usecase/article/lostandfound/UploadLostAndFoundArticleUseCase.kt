package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class UploadLostAndFoundArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(articleLostAndFoundList: List<ArticleLostAndFoundUpload>): Result<ArticleLostAndFound> =
        articleRepository.uploadArticleLostAndFound(articleLostAndFoundList)
}
