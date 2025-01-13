package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.feature.lostandfound.enums.ArticleBoardType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LostAndFoundViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<LostAndFoundState, LostAndFoundSideEffect> {
    override val container = container<LostAndFoundState, LostAndFoundSideEffect>(
        initialState = LostAndFoundState(),
        savedStateHandle = savedStateHandle
    )

    init {
        fetchMyKeyword()
        getUserType()
        fetchLostAndFoundList()
    }

    fun fetchLostAndFoundList() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    isLoading = true
                )
            }

            if (state.selectedKeyword.isEmpty()) {
                articleRepository.fetchArticleLostAndFoundPagination(
                    state.currentPage,
                    ARTICLES_PER_PAGE
                ).collectLatest {
                    reduce {
                        state.copy(
                            lostAndFoundList = it.articleLostAndFoundHeader.map { it.toLostAndFoundItemState() },
                            currentCount = it.currentCount,
                            totalCount = it.totalCount,
                            currentPage = it.currentPage,
                            totalPage = it.totalPage,
                            isLoading = false
                        )
                    }
                }
            } else {
                articleRepository.fetchSearchedArticles(
                    state.selectedKeyword,
                    ArticleBoardType.LOSTANDFOUND.id,
                    state.currentPage,
                    ARTICLES_PER_PAGE
                ).collectLatest {
                    reduce {
                        state.copy(
                            lostAndFoundList = it.articleHeaders.map { it.toLostAndFoundItemState() },
                            currentCount = it.currentCount,
                            totalCount = it.totalCount,
                            currentPage = it.currentPage,
                            totalPage = it.totalPage
                        )
                    }
                }

                // Fetch content by id because our search API doesn't return content value
                state.lostAndFoundList.forEachIndexed { index, lostAndFoundItemState ->
                    articleRepository.fetchArticleLostAndFound(lostAndFoundItemState.id).collect {
                        reduce {
                            state.copy(
                                lostAndFoundList = state.lostAndFoundList.mapIndexed { i, item ->
                                    if (i == index) {
                                        item.copy(
                                            content = it.content ?: "",
                                        )
                                    } else {
                                        item
                                    }
                                }
                            )
                        }
                    }
                }

                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchMyKeyword() = viewModelScope.launch {
        articleRepository.fetchMyKeyword().collectLatest {
            intent {
                reduce {
                    state.copy(
                        myKeywords = it
                    )
                }
            }
        }
    }

    fun selectKeyword(it: String) {
        intent {
            reduce {
                state.copy(
                    selectedKeyword = it
                )
            }
        }
    }

    fun getUserType() = viewModelScope.launch {
        userRepository.getUserInfoFlow().collectLatest { user ->
            intent {
                reduce {
                    if (user is User.Student) {
                        state.copy(
                            isAnonymous = false,
                            userType = user.userType
                        )
                    } else {
                        state.copy(
                            isAnonymous = true,
                            userType = ""
                        )
                    }
                }
            }
        }
    }

    fun changePage(page: Int) {
        intent {
            reduce {
                state.copy(
                    currentPage = page
                )
            }
            postSideEffect(LostAndFoundSideEffect.PageChanged(page))
        }
    }

    companion object {
        private const val ARTICLES_PER_PAGE = 10
    }
}
