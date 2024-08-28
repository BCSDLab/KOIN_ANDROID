package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.repository.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    private val coroutineScope: CoroutineScope
) : ArticleRepository {

    private val _myKeywords = MutableStateFlow<List<String>>(emptyList())
    private val myKeywords = _myKeywords.flatMapLatest {
        flow {
            val keywords = articleRemoteDataSource.fetchMyKeyword().keywords.map {
                it.keyword
            }
            emit(keywords)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

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
        return myKeywords
    }

    override fun fetchKeywordSuggestions(): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchKeywordSuggestions().keywords.map {
                it.keyword
            })
        }
    }

    override fun saveKeyword(keyword: String): Flow<Unit> {
        return flow {
            emit(articleRemoteDataSource.saveKeyword(keyword))
        }.onEach {
            _myKeywords.emit(buildList { addAll(_myKeywords.value); add(keyword) })
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