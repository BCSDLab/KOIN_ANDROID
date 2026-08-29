package `in`.koreatech.koin.feature.recruitment.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class RecruitmentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<RecruitmentDetailState, RecruitmentDetailSideEffect> {

    private val recruitmentId =
        savedStateHandle.toRoute<RecruitmentNavType.RecruitmentDetail>().recruitmentId

    override val container = container<RecruitmentDetailState, RecruitmentDetailSideEffect>(
        RecruitmentDetailState(id = recruitmentId)
    )

    fun updateMoreMenuVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(isMoreMenuVisible = visible) }
    }

    fun updateDeleteDialogVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(isDeleteDialogVisible = visible, isMoreMenuVisible = false) }
    }
}
