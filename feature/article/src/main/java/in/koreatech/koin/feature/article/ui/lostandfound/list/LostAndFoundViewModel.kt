package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.article.FetchMyKeywordUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticlePaginationUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchSearchedLostAndFoundArticlesUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
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
    private val fetchSearchedLostAndFoundArticlesUseCase: FetchSearchedLostAndFoundArticlesUseCase,
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
                    fetchSearchedLostAndFoundArticlesUseCase(
                        state.selectedKeyword,
                        state.currentPage,
                        ARTICLES_PER_PAGE
                    ).collectLatest {
                        reduce {
                            state.copy(
                                lostAndFoundList = it.articleLostAndFoundHeader.map { it.toLostAndFoundItemState() },
                                currentCount = it.currentCount,
                                totalCount = it.totalCount,
                                currentPage = it.currentPage,
                                totalPage = it.totalPage
                            )
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
                    when (user) {
                        is User.Student -> reduce {
                            state.copy(
                                isAnonymous = false,
                                userType = user.userType
                            )
                        }

                        is User.General -> reduce {
                            state.copy(
                                isAnonymous = false,
                                userType = user.userType
                            )
                        }
                        User.Anonymous -> reduce {
                            state.copy(
                                isAnonymous = true
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
