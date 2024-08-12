package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {

    override suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePagination {
        return articleRemoteDataSource.fetchArticlePagination(boardId, page, limit).toArticlePagination()
    }

    override suspend fun fetchArticle(articleId: Int): Article {
        return articleRemoteDataSource.fetchArticle(articleId).toArticle()
    }

    override suspend fun fetchHotArticles(): List<Article> {
        return articleRemoteDataSource.fetchHotArticles().map { it.toArticle() }
    }

    override suspend fun fetchSearchedArticles(
        query: String,
        page: Int,
        limit: Int
    ): ArticlePagination {
        return articleRemoteDataSource.fetchSearchedArticles(query, page, limit).toArticlePagination()
    }

    override suspend fun fetchHotKeywords(count: Int): List<String> {
        return articleRemoteDataSource.fetchHotKeywords(count).keywords
    }
}