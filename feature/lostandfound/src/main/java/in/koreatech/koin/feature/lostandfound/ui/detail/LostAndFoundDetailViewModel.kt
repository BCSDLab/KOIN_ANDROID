package `in`.koreatech.koin.feature.lostandfound.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticleUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.model.toArticleHeaderState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LostAndFoundDetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val fetchLostAndFoundArticleUseCase: FetchLostAndFoundArticleUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundDetailState, LostAndFoundDetailSideEffect> {
    override val container =
        container<LostAndFoundDetailState, LostAndFoundDetailSideEffect>(LostAndFoundDetailState())

    init {
        fetchHotArticles()
    }

    fun fetchLostAndFoundDetail(articleId: Int) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    isLoading = true
                )
            }

            getUserStatusUseCase()
                .combine(fetchLostAndFoundArticleUseCase(articleId).map {
                    it.toLostAndFoundDetailState()
                }) { user, article ->
                    user to article
                }.collectLatest { (user, article) ->
                if (user is User.Student) {
                    reduce {
                        state.copy(
                            currentLoggedInUser = user.name ?: ""
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            currentLoggedInUser = ""
                        )
                    }
                }

                reduce {
                    state.copy(
                        canDelete = state.currentLoggedInUser == article.author,
                        lostOrFound = article.lostOrFound,
                        id = article.id,
                        category = article.category,
                        foundPlace = article.foundPlace,
                        foundDate = article.foundDate,
                        content = article.content,
                        author = article.author,
                        images = article.images,
                        registeredAt = article.registeredAt,
                        updatedAt = article.updatedAt,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchHotArticles() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    hotArticles = articleRepository.fetchHotArticleHeaders()
                        .map {
                            it.filterIndexed { index, _ ->
                                index < HOT_ARTICLE_COUNT
                            }.map { it.toArticleHeaderState() }
                        }.stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = listOf()
                        )
                )
            }
        }
    }

    fun deleteArticle() = viewModelScope.launch {
        intent {
            articleRepository.deleteArticleLostAndFound(state.id).onSuccess {
                postSideEffect(LostAndFoundDetailSideEffect.DeleteArticle(state.id))
            }.onFailure {
                postSideEffect(LostAndFoundDetailSideEffect.DeleteArticleFailed)
            }
        }
    }

    companion object {
        const val HOT_ARTICLE_COUNT = 4
    }
}
