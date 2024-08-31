package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleLocalDataSource @Inject constructor(
    private val articleDataStore: ArticleDataStore
) {

    fun fetchSearchHistory(): Flow<List<String>> {
        return articleDataStore.fetchSearchHistory()
    }

    suspend fun saveSearchHistory(keyword: String) {
        articleDataStore.saveSearchHistory(keyword)
    }
}