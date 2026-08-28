package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class MyRecruitmentViewModel @Inject constructor() : ViewModel(), ContainerHost<MyRecruitmentState, MyRecruitmentSideEffect> {

    override val container = container<MyRecruitmentState, MyRecruitmentSideEffect>(
        MyRecruitmentState()
    )

    fun showCloseDialog(postId: Long) = intent {
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
    }

    fun confirmClose() = intent {
        state.closeTargetPostId?.let { closeRecruitment(it) }
    }

    fun closeRecruitment(postId: Long) = intent {
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
