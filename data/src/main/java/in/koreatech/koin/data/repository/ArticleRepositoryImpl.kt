package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {

    override fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): Flow<ArticlePagination> {
        return flow {
            emit(articleRemoteDataSource.fetchArticlePagination(boardId, page, limit).toArticlePagination())
        }
    }

    override fun fetchArticle(articleId: Int, boardId: Int): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchPreviousArticle(articleId: Int, boardId: Int): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchPreviousArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchNextArticle(articleId: Int, boardId: Int): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchNextArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchHotArticleHeaders(): Flow<List<ArticleHeader>> {
        return flow {
            emit(articleRemoteDataSource.fetchHotArticles().map { it.toArticleHeader() })
        }
    }

    override fun fetchMyKeyword(): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchMyKeyword().keywords.map {
                it.keyword
            })
        }
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