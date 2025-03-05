package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject

class FetchMyKeywordUseCase
    @Inject
    constructor(
        private val articleRepository: ArticleRepository,
    ) {
        operator fun invoke(): Flow<List<String>> =
            articleRepository.fetchMyKeyword().retryWhen { cause, attempt ->
                if (attempt < 3) {
                    true
                } else {
                    throw cause
                }
            }
    }
