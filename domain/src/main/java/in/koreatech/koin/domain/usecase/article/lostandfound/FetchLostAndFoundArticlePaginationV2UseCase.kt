package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchLostAndFoundArticlePaginationV2UseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(
        filterParams: LostAndFoundFilterParams
    ): Flow<ArticleLostAndFoundPagination> = articleRepository.fetchArticleLostAndFoundPaginationV2(filterParams)
}
