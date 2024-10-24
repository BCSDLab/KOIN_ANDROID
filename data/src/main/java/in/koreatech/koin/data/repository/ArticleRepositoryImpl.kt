package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.source.local.ArticleLocalDataSource
import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    private val articleLocalDataSource: ArticleLocalDataSource,
    private val userRepository: UserRepository,
    private val coroutineScope: CoroutineScope
) : ArticleRepository {

    val user = userRepository.getUserInfoFlow().distinctUntilChanged()
        .onEach { user ->
            if (user.isStudent) {
                _myKeywords.emit(articleRemoteDataSource.fetchMyKeyword().keywords)
            } else {
                _myKeywords.emit(articleLocalDataSource.fetchMyKeyword().map {
                    ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, it)
                })
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = User.Anonymous
        )

    private val _myKeywords =
        MutableStateFlow<List<ArticleKeywordWrapperResponse.ArticleKeywordResponse>>(emptyList())
    private val myKeywords = _myKeywords.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val hotArticleHeaders: StateFlow<List<ArticleHeader>> = flow {
        emit(articleRemoteDataSource.fetchHotArticles().map { it.toArticleHeader() })
    }.catch {
        emit(emptyList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = listOf()
    )


    init {
        user.launchIn(coroutineScope)
    }

    override fun fetchArticlePagination(
        boardId: Int,
        page: Int,
        limit: Int
    ): Flow<ArticlePagination> {
        return flow {
            emit(
                articleRemoteDataSource.fetchArticlePagination(boardId, page, limit)
                    .toArticlePagination()
            )
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
        return hotArticleHeaders
    }

    override fun fetchMyKeyword(): Flow<List<String>> {
        return myKeywords.map { response ->
            response.map {
                it.keyword
            }
        }
    }

    override fun fetchKeywordSuggestions(): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchKeywordSuggestions().keywords)
        }
    }

    override fun saveKeyword(keyword: String): Flow<Unit> {
        return flow {
            if (user.value.isStudent)
                emit(articleRemoteDataSource.saveKeyword(keyword))
            else {
                articleLocalDataSource.saveKeyword(keyword)
                emit(ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, keyword))
            }
        }.onEach {
            _myKeywords.emit(buildList {
                addAll(myKeywords.value)
                add(it)
            })
        }.map { Unit }
    }

    override fun deleteKeyword(keyword: String): Flow<Unit> {
        return flow {
            if (user.value.isStudent)
                emit(articleRemoteDataSource.deleteKeyword(myKeywords.value.first { it.keyword == keyword }.id))
            else
                emit(articleLocalDataSource.deleteKeyword(keyword))
        }.onEach {
            _myKeywords.emit(buildList {
                myKeywords.value.forEach {
                    if (it.keyword != keyword)
                        add(it)
                }
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
            emit(
                articleRemoteDataSource.fetchSearchedArticles(query, boardId, page, limit)
                    .toArticlePagination()
            )
        }
    }

    override fun fetchMostSearchedKeywords(count: Int): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchMostSearchedKeywords(count).keywords)
        }
    }

    override fun fetchSearchHistory(): Flow<List<String>> {
        return articleLocalDataSource.fetchSearchHistory()
    }

    override fun saveSearchHistory(query: String): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.saveSearchHistory(query))
        }
    }

    override fun deleteSearchHistory(query: String): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.deleteSearchHistory(query))
        }
    }

    override fun clearSearchHistory(): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.clearSearchHistory())
        }
    }
}