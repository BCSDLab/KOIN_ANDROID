package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ArticleLocalDataSource @Inject constructor(
    private val articleDataStore: ArticleDataStore
) {
    fun fetchSearchHistory(): Flow<List<String>> {
        return articleDataStore.fetchSearchHistory()
    }

    suspend fun saveSearchHistory(keyword: String) {
        articleDataStore.saveSearchHistory(keyword)
    }

    suspend fun deleteSearchHistory(query: String) {
        articleDataStore.deleteSearchHistory(query)
    }

    suspend fun clearSearchHistory() {
        articleDataStore.clearSearchHistory()
    }

    suspend fun fetchMyKeyword(): List<String> {
        return articleDataStore.fetchMyKeyword()
    }

    suspend fun saveKeyword(keyword: String) {
        articleDataStore.saveKeyword(keyword)
    }

    suspend fun deleteKeyword(keyword: String) {
        articleDataStore.deleteKeyword(keyword)
    }

    suspend fun fetchKeywordNotiIndex(): Int {
        return articleDataStore.fetchKeywordNotiIndex()
    }

    suspend fun saveKeywordNotiIndex() {
        articleDataStore.saveKeywordNotiIndex()
    }
}
