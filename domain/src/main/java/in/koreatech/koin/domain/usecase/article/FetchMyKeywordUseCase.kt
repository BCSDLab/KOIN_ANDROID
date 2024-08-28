package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FetchMyKeywordUseCase @Inject constructor(
    articleRepository: ArticleRepository,
    dispatcher: CoroutineDispatcher
) {

    private val keywords: StateFlow<List<String>> =
        articleRepository.fetchMyKeyword().stateIn(
            scope = CoroutineScope(dispatcher),
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    operator fun invoke(): Flow<List<String>> {
        return keywords
    }
}