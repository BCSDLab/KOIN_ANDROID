package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.model.article.KeywordType
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SaveLostItemKeywordUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(keyword: String): Flow<Unit> =
        articleRepository.saveKeyword(KeywordType.LOST_ITEM, keyword)
}
