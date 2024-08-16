package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.html.ArticleContent
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {

    override suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePagination {
        return articleRemoteDataSource.fetchArticlePagination(boardId, page, limit).toArticlePagination()
    }

    override suspend fun fetchArticle(articleId: Int): ArticleContent {
        return articleRemoteDataSource.fetchArticle(articleId).toArticleContent()
    }

    override suspend fun fetchHotArticles(): List<ArticleHeader> {
        return articleRemoteDataSource.fetchHotArticles().map { it.toArticleHeader() }
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