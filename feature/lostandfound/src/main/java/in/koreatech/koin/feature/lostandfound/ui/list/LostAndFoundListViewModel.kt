package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticlePaginationV2UseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.AuthorFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.CategoryFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.FoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundSortType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.LostOrFoundFilterType
import `in`.koreatech.koin.feature.lostandfound.model.toLostAndFoundItemState
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetailViewModel.Companion.PAGE_SIZE
import javax.inject.Inject
import kotlin.collections.plus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class LostAndFoundListViewModel @Inject constructor(
    private val fetchLostAndFoundArticlePaginationV2UseCase: FetchLostAndFoundArticlePaginationV2UseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundListState, Nothing> {
    override val container = container<LostAndFoundListState, Nothing>(
        initialState = LostAndFoundListState()
    )

    companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }

    init {
        initUserInfo()
        fetchLostAndFoundItem()
        observeQuery()
    }

    private fun initUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().collectLatest {
            intent {
                when (it) {
                    is User.Student -> reduce {
                        state.copy(
                            isLoggedIn = true
                        )
                    }
                    is User.General -> reduce {
                        state.copy(
                            isLoggedIn = true
                        )
                    }
                    is User.Anonymous -> reduce {
                        state.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    fun fetchLostAndFoundItem() = intent {
        if (state.isLoading) return@intent
        reduce {
            state.copy(
                isLoading = true,
                isFirstPageLoading = true,
                isLoadingMoreArticles = true
            )
        }
        val type = if (state.lostOrFoundFilterType == LostOrFoundFilterType.ALL) {
            null
        } else {
            state.lostOrFoundFilterType.value
        }
        val filterParams = LostAndFoundFilterParams(
            page = 1,
            limit = PAGE_SIZE,
            category = state.categoryFilterType.value,
            foundStatus = state.foundFilterType.value,
            author = state.authorFilterType.value,
            type = type,
            sort = LostAndFoundSortType.LATEST.value,
            title = state.searchQuery
        )
        fetchLostAndFoundArticlePaginationV2UseCase(filterParams)
            .catch {
                reduce {
                    state.copy(
                        searchedArticles = persistentListOf(),
                        hasMoreArticles = false,
                        isLoadingMoreArticles = false,
                        isLoading = false,
                        isFirstPageLoading = false
                    )
                }
                Timber.e(it)
            }
            .collectLatest { pagination ->
                reduce {
                    state.copy(
                        searchedArticles = pagination.articleLostAndFoundHeader.map { it.toLostAndFoundItemState() }.toPersistentList(),
                        searchedArticlesCurrentPage = pagination.currentPage,
                        searchedArticlesTotalPage = pagination.totalPage,
                        hasMoreArticles = pagination.currentPage < pagination.totalPage,
                        isLoadingMoreArticles = false,
                        isLoading = false,
                        isFirstPageLoading = false
                    )
                }
            }
    }

    fun loadMoreLostAndFoundItem() = intent {
        if (state.isLoadingMoreArticles || !state.hasMoreArticles) return@intent

        reduce {
            state.copy(isLoadingMoreArticles = true)
        }

        val nextPage = state.searchedArticlesCurrentPage + 1
        val type = if (state.lostOrFoundFilterType == LostOrFoundFilterType.ALL) {
            null
        } else {
            state.lostOrFoundFilterType.value
        }
        val filterParams = LostAndFoundFilterParams(
            page = nextPage,
            limit = PAGE_SIZE,
            category = state.categoryFilterType.value,
            foundStatus = state.foundFilterType.value,
            author = state.authorFilterType.value,
            type = type,
            sort = LostAndFoundSortType.LATEST.value,
            title = state.searchQuery
        )

        fetchLostAndFoundArticlePaginationV2UseCase(filterParams)
            .catch {
                reduce {
                    state.copy(isLoadingMoreArticles = false)
                }
            }
            .collectLatest { pagination ->
                val filteredArticles = pagination.articleLostAndFoundHeader.map { it.toLostAndFoundItemState() }
                reduce {
                    state.copy(
                        searchedArticles = (state.searchedArticles + filteredArticles).toPersistentList(),
                        searchedArticlesCurrentPage = pagination.currentPage,
                        searchedArticlesTotalPage = pagination.totalPage,
                        hasMoreArticles = pagination.currentPage < pagination.totalPage,
                        isLoadingMoreArticles = false
                    )
                }
            }
    }

    fun setSearchFilter(
        authorFilterType: AuthorFilterType,
        lostOrFoundFilterType: LostOrFoundFilterType,
        categoryFilterType: CategoryFilterType,
        foundFilterType: FoundFilterType
    ) = intent {
        reduce {
            state.copy(
                authorFilterType = authorFilterType,
                lostOrFoundFilterType = lostOrFoundFilterType,
                categoryFilterType = categoryFilterType,
                foundFilterType = foundFilterType
            )
        }
    }

    fun setShowFilterBottomSheet(value: Boolean) = intent {
        reduce {
            state.copy(
                showFilterBottomSheet = value
            )
        }
    }

    fun setShowWriteBottomSheet(value: Boolean) = intent {
        reduce {
            state.copy(
                showWriteBottomSheet = value
            )
        }
    }

    fun setShowFilterLoginDialog(show: Boolean) = intent {
        reduce {
            state.copy(
                showFilterLoginDialog = show
            )
        }
    }

    fun setShowWriteLoginDialog(show: Boolean) = intent {
        reduce {
            state.copy(
                showWriteLoginDialog = show
            )
        }
    }

    fun setSearchQuery(query: String) = intent {
        reduce {
            state.copy(
                searchQuery = query
            )
        }
    }

    private fun observeQuery() {
        container.stateFlow
            .map { it.searchQuery }
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { query ->
                fetchLostAndFoundItem()
            }
            .launchIn(viewModelScope)
    }
}
