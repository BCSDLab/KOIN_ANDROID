package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticlePaginationV2UseCase
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundSortType
import `in`.koreatech.koin.feature.lostandfound.model.toLostAndFoundItemState
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetailViewModel.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class LostAndFoundListViewModel @Inject constructor(
    private val fetchLostAndFoundArticlePaginationV2UseCase: FetchLostAndFoundArticlePaginationV2UseCase,
): ViewModel(), ContainerHost<LostAndFoundListState, Nothing>  {
    override val container = container<LostAndFoundListState, Nothing>(
        initialState = LostAndFoundListState()
    )

    init {
        fetchLostAndFoundItem()
    }

    fun fetchLostAndFoundItem() = intent {
        if(state.isLoading) return@intent
        reduce {
            state.copy(
                isLoading = true,
                isLoadingMoreArticles = true
            )
        }
        val filterParams = LostAndFoundFilterParams(
            page = 1,
            limit = PAGE_SIZE,
            sort = LostAndFoundSortType.LATEST.value
        )
        fetchLostAndFoundArticlePaginationV2UseCase(filterParams)
            .catch {
                reduce {
                    state.copy(
                        isLoadingMoreArticles = false,
                        isLoading = false
                    )
                }
                Timber.e(it)
            }
            .collectLatest { pagination ->
                reduce {
                    state.copy(
                        searchedArticles = pagination.articleLostAndFoundHeader.map { it.toLostAndFoundItemState() },
                        searchedArticlesCurrentPage = pagination.currentPage,
                        searchedArticlesTotalPage = pagination.totalPage,
                        hasMoreArticles = pagination.currentPage < pagination.totalPage,
                        isLoadingMoreArticles = false,
                        isLoading = false
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
        val filterParams = LostAndFoundFilterParams(
            page = nextPage,
            limit = PAGE_SIZE,
            sort = LostAndFoundSortType.LATEST.value
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
                        searchedArticles = state.searchedArticles + filteredArticles,
                        searchedArticlesCurrentPage = pagination.currentPage,
                        searchedArticlesTotalPage = pagination.totalPage,
                        hasMoreArticles = pagination.currentPage < pagination.totalPage,
                        isLoadingMoreArticles = false
                    )
                }
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
}