package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.article.ArticleModifyRequest
import `in`.koreatech.koin.data.request.article.toRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.source.local.ArticleLocalDataSource
import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.article.KoinArticleException
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundStats
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.KeywordType
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException

class ArticleRepositoryImpl @Inject constructor(
    private val articleRemoteDataSource: ArticleRemoteDataSource,
    private val articleLocalDataSource: ArticleLocalDataSource,
    private val userRepository: UserRepository,
    private val coroutineScope: CoroutineScope
) : ArticleRepository {
    val user = userRepository.getUserInfoFlow().distinctUntilChanged()
        .onEach { user ->
            if (user.isStudent || user.isGeneral) {
                runCatching {
                    articleRemoteDataSource.fetchMyKeyword(KeywordType.KOREATECH).keywords
                }.onSuccess { _myArticleKeywords.emit(it) }
                    .onFailure {
                        if (it is CancellationException) throw it
                        _myArticleKeywords.emit(emptyList())
                    }

                runCatching {
                    articleRemoteDataSource.fetchMyKeyword(KeywordType.LOST_ITEM_KEYWORD).keywords
                }.onSuccess { _myLostItemKeywords.emit(it) }
                    .onFailure {
                        if (it is CancellationException) throw it
                        _myLostItemKeywords.emit(emptyList())
                    }
            } else {
                _myArticleKeywords.emit(
                    articleLocalDataSource.fetchMyKeyword(KeywordType.KOREATECH).map {
                        ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, it)
                    }
                )
                _myLostItemKeywords.emit(
                    articleLocalDataSource.fetchMyKeyword(KeywordType.LOST_ITEM_KEYWORD).map {
                        ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, it)
                    }
                )
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = User.Anonymous
        )

    private val _myArticleKeywords =
        MutableStateFlow<List<ArticleKeywordWrapperResponse.ArticleKeywordResponse>>(emptyList())
    private val myArticleKeywords = _myArticleKeywords.asStateFlow()

    private val _myLostItemKeywords =
        MutableStateFlow<List<ArticleKeywordWrapperResponse.ArticleKeywordResponse>>(emptyList())
    private val myLostItemKeywords = _myLostItemKeywords.asStateFlow()

    private val hotArticleHeaders: StateFlow<List<ArticleHeader>> =
        flow {
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

    override fun fetchArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchPreviousArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchPreviousArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchNextArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article> {
        return flow {
            emit(articleRemoteDataSource.fetchNextArticle(articleId, boardId).toArticle())
        }
    }

    override fun fetchHotArticleHeaders(): Flow<List<ArticleHeader>> {
        return hotArticleHeaders
    }

    override fun fetchMyKeyword(): Flow<List<String>> {
        return myArticleKeywords.map { response ->
            response.map {
                it.keyword
            }
        }
    }

    override fun fetchMyLostItemKeyword(): Flow<List<String>> {
        return myLostItemKeywords.map { response -> response.map { it.keyword } }
    }

    override fun fetchKeywordSuggestions(type: KeywordType): Flow<List<String>> {
        return flow {
            emit(articleRemoteDataSource.fetchKeywordSuggestions(type).keywords)
        }
    }

    override fun saveKeyword(type: KeywordType, keyword: String): Flow<Unit> {
        val keywords = when (type) {
            KeywordType.KOREATECH -> myArticleKeywords
            KeywordType.LOST_ITEM_KEYWORD -> myLostItemKeywords
        }

        val mutableKeywords = when (type) {
            KeywordType.KOREATECH -> _myArticleKeywords
            KeywordType.LOST_ITEM_KEYWORD -> _myLostItemKeywords
        }

        return flow {
            if (user.value.isStudent || user.value.isGeneral) {
                emit(articleRemoteDataSource.saveKeyword(type, keyword))
            } else {
                articleLocalDataSource.saveKeyword(type, keyword)
                emit(ArticleKeywordWrapperResponse.ArticleKeywordResponse(0, keyword))
            }
        }.onEach {
            mutableKeywords.emit(
                buildList {
                    addAll(keywords.value)
                    add(it)
                }
            )
        }.map { Unit }
    }

    override fun deleteKeyword(type: KeywordType, keyword: String): Flow<Unit> {
        val keywords = when (type) {
            KeywordType.KOREATECH -> myArticleKeywords
            KeywordType.LOST_ITEM_KEYWORD -> myLostItemKeywords
        }

        val mutableKeywords = when (type) {
            KeywordType.KOREATECH -> _myArticleKeywords
            KeywordType.LOST_ITEM_KEYWORD -> _myLostItemKeywords
        }

        return flow {
            if (user.value.isStudent || user.value.isGeneral) {
                val targetKeyword = keywords.value.firstOrNull { it.keyword == keyword }
                if (targetKeyword != null) {
                    emit(articleRemoteDataSource.deleteKeyword(targetKeyword.id))
                } else {
                    // 로컬에 존재하지 않는 경우에도 성공 처리하여 하위 onEach가 실행되도록 보장
                    emit(Unit)
                }
            } else {
                emit(articleLocalDataSource.deleteKeyword(type, keyword))
            }
        }.onEach {
            mutableKeywords.emit(
                buildList {
                    keywords.value.forEach {
                        if (it.keyword != keyword) {
                            add(it)
                        }
                    }
                }
            )
        }
    }

    override fun fetchKeywordNotiIndex(): Flow<Int> {
        return flow {
            emit(articleLocalDataSource.fetchKeywordNotiIndex())
        }
    }

    override fun saveKeywordNotiIndex(): Flow<Unit> {
        return flow {
            emit(articleLocalDataSource.saveKeywordNotiIndex())
        }
    }

    override fun fetchSearchedArticles(
        query: String,
        boardId: Int?,
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

    override fun fetchArticleLostAndFoundPaginationV2(
        filterParams: LostAndFoundFilterParams
    ): Flow<ArticleLostAndFoundPagination> {
        return flow {
            emit(
                articleRemoteDataSource.fetchArticleLostAndFoundPaginationV2(
                    type = filterParams.type,
                    page = filterParams.page,
                    limit = filterParams.limit,
                    category = filterParams.category,
                    foundStatus = filterParams.foundStatus,
                    sort = filterParams.sort,
                    author = filterParams.author,
                    title = filterParams.title
                ).toArticleLostAndFoundPagination()
            )
        }
    }

    override fun fetchSearchedLostAndFoundArticles(query: String, page: Int, limit: Int): Flow<ArticleLostAndFoundPagination> {
        return flow {
            emit(
                articleRemoteDataSource.fetchSearchedLostAndFoundArticles(query, page, limit)
                    .toArticleLostAndFoundPagination()
            )
        }
    }

    override fun fetchArticleLostAndFoundV2(articleId: Int): Flow<ArticleLostAndFound> {
        return flow {
            emit(
                articleRemoteDataSource.fetchArticleLostAndFoundV2(articleId).toArticleLostAndFound()
            )
        }
    }

    override suspend fun uploadArticleLostAndFound(
        articleLostAndFoundList: List<ArticleLostAndFoundUpload>
    ): Result<ArticleLostAndFound> {
        return articleRemoteDataSource.uploadArticleLostAndFound(articleLostAndFoundList).map { it.toArticleLostAndFound() }
    }

    override suspend fun deleteArticleLostAndFound(articleId: Int): Result<Unit> {
        return articleRemoteDataSource.deleteArticleLostAndFound(articleId)
    }

    override suspend fun reportLostAndFoundArticle(
        articleId: Int,
        articleLostAndFoundList: List<ArticleLostAndFoundReportItem>
    ): Result<Unit> {
        return articleRemoteDataSource.reportLostAndFoundArticle(articleId, articleLostAndFoundList.toRequest())
    }

    override suspend fun fetchArticleLostAndFoundStats(): Result<ArticleLostAndFoundStats> {
        return runCatching {
            articleRemoteDataSource.fetchArticleLostAndFoundStats().toArticleLostAndFoundStats()
        }.mapHttpFailure { }
    }

    override suspend fun updateItemFound(
        articleId: Int
    ): Result<Unit> {
        return runCatching {
            val response = articleRemoteDataSource.updateItemFound(articleId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.mapHttpFailure { }
    }

    override suspend fun modifyArticleLostAndFound(
        articleId: Int,
        category: String,
        foundPlace: String,
        foundDate: String,
        content: String?,
        newImage: List<String>?,
        deleteImageIds: List<Int>?
    ): Result<Unit> {
        return runCatching {
            val response = articleRemoteDataSource.modifyArticleLostAndFound(
                articleId,
                ArticleModifyRequest(
                    category,
                    foundPlace,
                    foundDate,
                    content,
                    newImage,
                    deleteImageIds
                )
            )
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.mapHttpFailure {
            on(400) throws KoinArticleException.CanNotFoundItemException()
            on(401) throws KoinArticleException.UnauthorizedUserException()
            on(403) throws KoinArticleException.ForbiddenAuthor()
            on(404) throws KoinArticleException.NotFoundImage()
        }
    }
}
