package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class FetchSearchHistoryUseCase @Inject constructor(
    private val articleRepository: ArticleRepository

) {
    operator fun invoke(): Flow<List<String>> {
        return articleRepository.fetchSearchHistory().transform {
            if (it.size > MAX_SEARCH_HISTORY_COUNT)
                articleRepository.deleteSearchHistory(it.last())
            else emit(it)
        }
    }

    companion object {
        const val MAX_SEARCH_HISTORY_COUNT = 5
    }
}