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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    private val articleLocalDataSource: ArticleLocalDataSource,
    private val userRepository: UserRepository,
    coroutineScope: CoroutineScope
) : ArticleRepository {

    val user = userRepository.getUserInfoFlow().stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = User.Anonymous
    )

    private val _myKeywords = MutableStateFlow<List<ArticleKeywordWrapperResponse.ArticleKeywordResponse>>(emptyList())
    private val myKeywords = _myKeywords.flatMapLatest {
        if (user.value.isStudent) {
            if (_myKeywords.value.isEmpty()) {
                val keywords = articleRemoteDataSource.fetchMyKeyword().keywords
                flowOf(keywords)
            } else flowOf(it)

        } else {
            if (_myKeywords.value.isEmpty()) {
                val keywords = articleLocalDataSource.fetchMyKeyword().map { list ->
                    list.map {
                        ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, it)
                    }
                }
                keywords
            } else flowOf(it)
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
        }.map { Unit }.catch {
            println("ddddddd   " + it)
        }
    }

    override fun deleteKeyword(keyword: String): Flow<Unit> {
        return flow {
            if (user.value.isStudent)
                emit(articleRemoteDataSource.deleteKeyword(myKeywords.value.first { it.keyword == keyword }.id))
            else
                emit(articleLocalDataSource.deleteKeyword(keyword))
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

    override fun fetchSearchHistory(): Flow<List<String>> {
        return articleLocalDataSource.fetchSearchHistory()
    }

    override fun saveSearchHistory(query: String): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.saveSearchHistory(query))
        }
    }

    override fun deleteSearchHistory(vararg query: String): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.deleteSearchHistory(*query))
        }
    }
}