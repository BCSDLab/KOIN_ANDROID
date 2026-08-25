package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterState
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class MyAppliedRecruitmentViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<MyAppliedRecruitmentState, MyAppliedRecruitmentSideEffect> {

    override val container = container<MyAppliedRecruitmentState, MyAppliedRecruitmentSideEffect>(
        MyAppliedRecruitmentState()
    )

    fun showFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = true) }
    }

    fun dismissFilterSheet() = intent {
        reduce { state.copy(showFilterSheet = false) }
    }

    fun applyFilter(filter: AppliedFilterState) = intent {
        reduce { state.copy(filter = filter, showFilterSheet = false) }
    }
}
