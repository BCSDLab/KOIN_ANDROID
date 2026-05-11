package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticlePaginationV2UseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostItemKeywordUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.AuthorFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.CategoryFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.FoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.LostOrFoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundSortType
import `in`.koreatech.koin.feature.lostandfound.model.toLostAndFoundItemState
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetailViewModel.Companion.PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@Suppress("TooManyFunctions")
@HiltViewModel
class LostAndFoundListViewModel @Inject constructor(
    private val fetchLostAndFoundArticlePaginationV2UseCase: FetchLostAndFoundArticlePaginationV2UseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val fetchLostItemKeywordUseCase: FetchLostItemKeywordUseCase
) : ViewModel(), ContainerHost<LostAndFoundListState, LostAndFoundListSideEffect> {
    override val container: Container<LostAndFoundListState, LostAndFoundListSideEffect> = container(
        initialState = LostAndFoundListState()
    )

    init {
        initUserInfo()
        observeKeywords()
    }

    private fun initUserInfo() = intent {
        repeatOnSubscription {
            getUserStatusUseCase()
                .catch {
                    // 사용자 상태 로드 실패 — 익명으로 처리
                    reduce { state.copy(isLoggedIn = false) }
                }
                .collect { user ->
                    when (user) {
                        is User.Student -> reduce { state.copy(isLoggedIn = true) }
                        is User.General -> reduce { state.copy(isLoggedIn = true) }
                        is User.Anonymous -> reduce { state.copy(isLoggedIn = false) }
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
            category = state.categoryFilterType.map { it.value },
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
            category = state.categoryFilterType.map { it.value },
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
                        searchedArticles = (state.searchedArticles + filteredArticles).distinctBy { it.id }.toPersistentList(),
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
        categoryFilterType: List<CategoryFilterType>,
        foundFilterType: FoundFilterType
    ) = intent {
        if (!state.isLoggedIn && authorFilterType == AuthorFilterType.MY) {
            postSideEffect(LostAndFoundListSideEffect.UpdateSignInDialog(true))
            return@intent
        }

        reduce {
            state.copy(
                authorFilterType = authorFilterType,
                lostOrFoundFilterType = lostOrFoundFilterType,
                categoryFilterType = categoryFilterType.toPersistentList(),
                foundFilterType = foundFilterType
            )
        }
        postSideEffect(LostAndFoundListSideEffect.FetchData)
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

    fun selectKeyword(index: Int) {
        intent {
            reduce { state.copy(selectedKeywordIndex = index) }
        }
    }

    private fun observeKeywords() = intent {
        repeatOnSubscription {
            fetchLostItemKeywordUseCase()
                .catch { reduce { state.copy(keywords = persistentListOf(), selectedKeywordIndex = 0) } }
                .collect { keywords ->
                    // UI 칩 인덱스 체계: 0 = "전체보기", 1 = keywords[0], ...
                    // 따라서 실제 선택된 키워드 = keywords[selectedKeywordIndex - 1]
                    val oldSelectedKeyword = if (state.selectedKeywordIndex > 0) {
                        state.keywords.getOrNull(state.selectedKeywordIndex - 1)
                    } else {
                        null
                    }
                    // 새 목록에서 복원 시 +1 offset 적용
                    val newIndex = oldSelectedKeyword?.let { kw ->
                        val idx = keywords.indexOf(kw)
                        if (idx >= 0) idx + 1 else 0
                    } ?: 0
                    reduce {
                        state.copy(
                            keywords = keywords.toPersistentList(),
                            selectedKeywordIndex = newIndex
                        )
                    }
                    if (oldSelectedKeyword != keywords.getOrNull(newIndex - 1)) {
                        postSideEffect(LostAndFoundListSideEffect.FetchData)
                    }
                }
        }
    }
}
