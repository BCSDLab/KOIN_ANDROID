package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.recruitment.CloseRecruitmentPostUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.GetMyRecruitmentPostsUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.toApiValue
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.toMyRecruitmentPost
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class MyRecruitmentViewModel @Inject constructor(
    private val getMyRecruitmentPostsUseCase: GetMyRecruitmentPostsUseCase,
    private val closeRecruitmentPostUseCase: CloseRecruitmentPostUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<MyRecruitmentState, MyRecruitmentSideEffect> {

    override val container: Container<MyRecruitmentState, MyRecruitmentSideEffect> =
        container(MyRecruitmentState())

    init {
        checkLoginAndLoad()
    }

    private fun checkLoginAndLoad() = intent {
        val user = getUserStatusUseCase().first()
        if (user is User.Anonymous) {
            postSideEffect(MyRecruitmentSideEffect.NavigateToLogin)
        } else {
            loadMyRecruitmentPosts(state.filter)
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun loadMyRecruitmentPosts(filter: RecruitmentFilterState) = subIntent {
        reduce { state.copy(isLoading = true) }
        getMyRecruitmentPostsUseCase(
            status = filter.status.toApiValue(),
            sort = filter.sort.toApiValue(),
            page = 1,
            limit = MY_RECRUITMENT_PAGE_SIZE
        ).onSuccess { result ->
            reduce {
                state.copy(
                    isLoading = false,
                    posts = result.posts.map { it.toMyRecruitmentPost() }.toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    fun loadMoreMyRecruitmentPosts() = intent {
        if (state.isLoadingMore || state.currentPage >= state.totalPage) return@intent
        reduce { state.copy(isLoadingMore = true) }
        getMyRecruitmentPostsUseCase(
            status = state.filter.status.toApiValue(),
            sort = state.filter.sort.toApiValue(),
            page = state.currentPage + 1,
            limit = MY_RECRUITMENT_PAGE_SIZE
        ).onSuccess { result ->
            reduce {
                state.copy(
                    isLoadingMore = false,
                    posts = (state.posts + result.posts.map { it.toMyRecruitmentPost() }).toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage
                )
            }
        }.onFailure {
            reduce { state.copy(isLoadingMore = false) }
        }
    }

    fun showCloseDialog(postId: Int) = intent {
        reduce { state.copy(showCloseDialog = true, closeTargetPostId = postId) }
    }

    fun dismissCloseDialog() = intent {
        reduce { state.copy(showCloseDialog = false, closeTargetPostId = null) }
    }

    fun showFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = true) }
    }

    fun dismissFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = false) }
    }

    fun applyFilter(filter: RecruitmentFilterState) = intent {
        reduce { state.copy(filter = filter, showFilterSheet = false) }
        loadMyRecruitmentPosts(filter)
    }

    fun confirmClose() = intent {
        val postId = state.closeTargetPostId ?: return@intent
        closeRecruitmentPostUseCase(postId).onSuccess {
            reduce {
                state.copy(
                    showCloseDialog = false,
                    closeTargetPostId = null,
                    posts = state.posts.map { post ->
                        if (post.id == postId) post.copy(status = RecruitmentStatus.Complete) else post
                    }.toPersistentList()
                )
            }
        }.onFailure {
            reduce { state.copy(showCloseDialog = false, closeTargetPostId = null) }
        }
    }

    companion object {
        private const val MY_RECRUITMENT_PAGE_SIZE = 10
    }
}
