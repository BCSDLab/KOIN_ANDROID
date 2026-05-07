package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.ArticleDataStore
import `in`.koreatech.koin.domain.model.article.KeywordType
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

    suspend fun fetchMyKeyword(type: KeywordType): List<String> {
        return articleDataStore.fetchMyKeyword(type)
    }

    suspend fun saveKeyword(type: KeywordType, keyword: String) {
        articleDataStore.saveKeyword(type, keyword)
    }

    suspend fun deleteKeyword(type: KeywordType, keyword: String) {
        articleDataStore.deleteKeyword(type, keyword)
    }

    suspend fun fetchKeywordNotiIndex(): Int {
        return articleDataStore.fetchKeywordNotiIndex()
    }

    suspend fun saveKeywordNotiIndex() {
        articleDataStore.saveKeywordNotiIndex()
    }
}
