package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMyKeywordUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(): Flow<List<String>> =
        articleRepository.fetchMyKeyword()
}
