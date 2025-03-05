package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class ReportLostAndFoundArticleUseCase
    @Inject
    constructor(
        private val articleRepository: ArticleRepository,
    ) {
        suspend operator fun invoke(
            articleId: Int,
            articleLostAndFoundReportItem: List<ArticleLostAndFoundReportItem>,
        ): Result<Unit> {
            return articleRepository.reportLostAndFoundArticle(articleId, articleLostAndFoundReportItem)
        }
    }
