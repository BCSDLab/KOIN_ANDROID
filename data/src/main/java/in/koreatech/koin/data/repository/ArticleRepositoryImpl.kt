package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    coroutineScope: CoroutineScope
) : ArticleRepository {

    private val _myKeywords = MutableStateFlow<List<ArticleKeywordWrapperResponse.ArticleKeywordResponse>>(emptyList())
    private val myKeywords = _myKeywords.flatMapLatest {
        if (_myKeywords.value.isEmpty()) {
            val keywords = articleRemoteDataSource.fetchMyKeyword().keywords
            flowOf(keywords)
        } else {
            flowOf(it)
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
        return myKeywords.map { response ->
            response.map { it.keyword }
        }
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
            _myKeywords.emit(buildList {
                addAll(myKeywords.value)
                add(it)
            })
        }.map { Unit }
    }

    override fun deleteKeyword(keyword: String): Flow<Unit> {
        return flow {
            emit(articleRemoteDataSource.deleteKeyword(keyword))
        }.onEach {
            _myKeywords.emit(buildList {
                addAll(myKeywords.value)
                remove(myKeywords.value.first { it.keyword == keyword })
            })
        }
    }

    override fun fetchSearchedArticles(
        query: String,
        boardId: Int,
        page: Int,
        limit: Int
    ): Flow<ArticlePagination> {
        return flow {
            emit(articleRemoteDataSource.fetchSearchedArticles(query, boardId, page, limit).toArticlePagination())
        }
    }

    override fun fetchMostSearchedKeywords(count: Int): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchMostSearchedKeywords(count).keywords)
        }
    }
}