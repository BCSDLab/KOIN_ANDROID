package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticlePagination

interface ArticleRepository {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePagination
    suspend fun fetchArticle(articleId: Int): Article
    suspend fun fetchHotArticles(): List<Article>
    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePagination
    suspend fun fetchHotKeywords(count: Int): List<String>
}