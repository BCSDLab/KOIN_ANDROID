package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.article.FetchMyKeywordUseCase
import `in`.koreatech.koin.domain.usecase.article.FetchSearchedArticlesUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticlePaginationUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticleUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.enums.ArticleBoardType
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class LostAndFoundViewModel @Inject constructor(
    private val fetchLostAndFoundArticlePaginationUseCase: FetchLostAndFoundArticlePaginationUseCase,
    private val fetchSearchedArticlesUseCase: FetchSearchedArticlesUseCase,
    private val fetchLostAndFoundArticleUseCase: FetchLostAndFoundArticleUseCase,
    private val fetchMyKeywordUseCase: FetchMyKeywordUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<LostAndFoundState, LostAndFoundSideEffect> {
    override val container =
        container<LostAndFoundState, LostAndFoundSideEffect>(
            initialState = LostAndFoundState(),
            savedStateHandle = savedStateHandle
        )

    init {
        fetchLostAndFoundList()
        fetchMyKeyword()
        getUserType()
    }

    fun fetchLostAndFoundList() =
        viewModelScope.launch {
            intent {
                reduce {
                    state.copy(
                        isLoading = true
                    )
                }

                if (state.selectedKeyword.isEmpty()) {
                    fetchLostAndFoundArticlePaginationUseCase(
                        state.currentPage,
                        ARTICLES_PER_PAGE,
                        state.selectedType?.name
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
                    fetchSearchedArticlesUseCase(
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

                        // Fetch content by id because our search API doesn't return content value
                        state.lostAndFoundList.forEachIndexed { index, lostAndFoundItemState ->
                            fetchLostAndFoundArticleUseCase(lostAndFoundItemState.id).collect {
                                reduce {
                                    state.copy(
                                        lostAndFoundList =
                                        state.lostAndFoundList.mapIndexed { i, item ->
                                            if (i == index) {
                                                return@mapIndexed item.copy(
                                                    content = it.content ?: ""
                                                )
                                            } else {
                                                item
                                            }
                                        }
                                    )
                                }
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

    fun fetchMyKeyword() =
        viewModelScope.launch {
            fetchMyKeywordUseCase().catch {
                intent {
                    reduce {
                        state.copy(
                            myKeywords = emptyList()
                        )
                    }
                    Timber.d("Failed to fetch my keywords $it")
                }
                throw it
            }.collectLatest {
                intent {
                    reduce {
                        state.copy(
                            myKeywords = it
                        )
                    }
                    postSideEffect(LostAndFoundSideEffect.KeywordUpdated)
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

    fun getUserType() =
        viewModelScope.launch {
            getUserStatusUseCase().collectLatest { user ->
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

    fun setShowLoginRequestDialog(showDialog: Boolean) =
        intent {
            reduce {
                state.copy(
                    showLoginRequestDialog = showDialog
                )
            }
        }

    fun setFabDialogExpanded(isExpanded: Boolean) =
        intent {
            reduce {
                state.copy(
                    isFabDialogExpanded = isExpanded
                )
            }
        }

    fun setDropdownExpanded(isExpanded: Boolean) =
        intent {
            reduce {
                state.copy(
                    isDropdownExpanded = isExpanded
                )
            }
        }

    fun setSelectedType(type: LostOrFoundType?) =
        intent {
            reduce {
                state.copy(
                    selectedType = type
                )
            }
        }

    companion object {
        private const val ARTICLES_PER_PAGE = 10
    }
}
