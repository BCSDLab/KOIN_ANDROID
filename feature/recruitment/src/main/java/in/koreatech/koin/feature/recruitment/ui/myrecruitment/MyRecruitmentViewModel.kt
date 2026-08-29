package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.CloseRecruitmentPostUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.GetMyRecruitmentPostsUseCase
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.toApiValue
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.toMyRecruitmentPost
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class MyRecruitmentViewModel @Inject constructor(
    private val getMyRecruitmentPostsUseCase: GetMyRecruitmentPostsUseCase,
    private val closeRecruitmentPostUseCase: CloseRecruitmentPostUseCase
) : ViewModel(), ContainerHost<MyRecruitmentState, MyRecruitmentSideEffect> {

    override val container: Container<MyRecruitmentState, MyRecruitmentSideEffect> =
        container(MyRecruitmentState())

    init {
        loadMyRecruitmentPosts(RecruitmentFilterState())
    }

    private fun loadMyRecruitmentPosts(filter: RecruitmentFilterState) = intent {
        getMyRecruitmentPostsUseCase(
            status = filter.status.toApiValue(),
            sort = filter.sort.toApiValue()
        ).onSuccess { posts ->
            reduce { state.copy(posts = posts.map { it.toMyRecruitmentPost() }.toPersistentList()) }
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
        }
    }
}
