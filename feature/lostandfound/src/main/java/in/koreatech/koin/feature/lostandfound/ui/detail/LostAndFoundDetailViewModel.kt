package `in`.koreatech.koin.feature.lostandfound.ui.detail

import android.webkit.URLUtil
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.article.lostandfound.DeleteArticleLostAndFoundUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchHotArticlesUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticleUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.model.toArticleHeaderState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LostAndFoundDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchLostAndFoundArticleUseCase: FetchLostAndFoundArticleUseCase,
    private val fetchHotArticlesUseCase: FetchHotArticlesUseCase,
    private val deleteArticleLostAndFoundUseCase: DeleteArticleLostAndFoundUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundDetailState, LostAndFoundDetailSideEffect> {
    override val container =
        container<LostAndFoundDetailState, LostAndFoundDetailSideEffect>(LostAndFoundDetailState(), savedStateHandle) {
            val articleId = savedStateHandle.get<Int>(ARTICLE_ID)
            checkNotNull(articleId)
            fetchLostAndFoundDetail(articleId)
        }

    init {
        initUserInfo()
        fetchHotArticles()
    }

    private fun initUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().collectLatest {
            intent {
                if (it is User.Student) {
                    reduce {
                        state.copy(
                            isLoggedIn = true,
                            currentLoggedInUser = it.nickname ?: ""
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            isLoggedIn = false,
                        )
                    }
                }
            }
        }
    }

    fun fetchLostAndFoundDetail(articleId: Int) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    isLoading = true
                )
            }
            fetchLostAndFoundArticleUseCase(articleId).catch {
                if (it is HttpException && it.code() == 404) {
                    postSideEffect(LostAndFoundDetailSideEffect.DeletedArticle)
                }
            }.map {
                it.toLostAndFoundDetailState()
            }.collectLatest { article ->
                reduce {
                    state.copy(
                        lostOrFound = article.lostOrFound,
                        id = article.id,
                        category = article.category,
                        foundPlace = article.foundPlace,
                        foundDate = article.foundDate,
                        content = article.content,
                        author = article.author,
                        images = article.images?.filter { URLUtil.isValidUrl(it.toString()) },
                        registeredAt = article.registeredAt,
                        updatedAt = article.updatedAt,
                        isWriterCouncil = article.isWriterCouncil,
                        isMine = state.currentLoggedInUser == article.author,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchHotArticles() = viewModelScope.launch {
        intent {
            fetchHotArticlesUseCase().collectLatest {
                reduce {
                    state.copy(
                        hotArticles = it.filterIndexed { index, _ -> index < HOT_ARTICLE_COUNT }
                            .map { it.toArticleHeaderState() }
                    )
                }
            }
        }
    }

    fun deleteArticle() = viewModelScope.launch {
        intent {
            deleteArticleLostAndFoundUseCase(state.id).onSuccess {
                postSideEffect(LostAndFoundDetailSideEffect.DeleteArticle(state.id))
            }.onFailure {
                postSideEffect(LostAndFoundDetailSideEffect.DeleteArticleFailed)
            }
        }
    }

    fun setShowDeleteDialog(show: Boolean) = intent {
        reduce {
            state.copy(
                showDeleteDialog = show
            )
        }
    }

    companion object {
        const val HOT_ARTICLE_COUNT = 4
        const val ARTICLE_ID = "article_id"
    }
}
