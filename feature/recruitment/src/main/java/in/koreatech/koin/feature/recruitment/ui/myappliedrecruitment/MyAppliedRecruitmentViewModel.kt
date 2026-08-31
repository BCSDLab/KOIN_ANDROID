package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.recruitment.GetMyAppliedRecruitmentsUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.toApiValue
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.toAppliedRecruitmentPost
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
class MyAppliedRecruitmentViewModel @Inject constructor(
    private val getMyAppliedRecruitmentsUseCase: GetMyAppliedRecruitmentsUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<MyAppliedRecruitmentState, MyAppliedRecruitmentSideEffect> {

    override val container: Container<MyAppliedRecruitmentState, MyAppliedRecruitmentSideEffect> =
        container(MyAppliedRecruitmentState())

    init {
        checkLoginAndLoad()
    }

    private fun checkLoginAndLoad() = intent {
        val user = getUserStatusUseCase().first()
        if (user is User.Anonymous) {
            postSideEffect(MyAppliedRecruitmentSideEffect.NavigateToLogin)
        } else {
            loadMyAppliedRecruitments(state.filter)
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun loadMyAppliedRecruitments(filter: AppliedFilterState) = subIntent {
        reduce { state.copy(isLoading = true) }
        getMyAppliedRecruitmentsUseCase(
            statuses = filter.status.toApiValue(),
            sort = filter.sort.toApiValue()
        ).onSuccess { applications ->
            reduce {
                state.copy(
                    isLoading = false,
                    posts = applications.map { it.toAppliedRecruitmentPost() }.toPersistentList()
                )
            }
        }
    }

    fun showFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = true) }
    }

    fun dismissFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = false) }
    }

    fun applyFilter(filter: AppliedFilterState) = intent {
        reduce { state.copy(filter = filter, showFilterSheet = false) }
        loadMyAppliedRecruitments(filter)
    }
}
