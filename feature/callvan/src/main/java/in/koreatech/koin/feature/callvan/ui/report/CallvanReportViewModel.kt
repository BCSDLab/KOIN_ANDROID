package `in`.koreatech.koin.feature.callvan.ui.report

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException
import `in`.koreatech.koin.domain.usecase.callvan.ReportCallvanUserUseCase
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CallvanReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reportCallvanUserUseCase: ReportCallvanUserUseCase
) : ViewModel(), ContainerHost<CallvanReportState, CallvanReportSideEffect> {

    override val container = container<CallvanReportState, CallvanReportSideEffect>(
        CallvanReportState()
    )

    private val postId: Int = savedStateHandle["postId"] ?: 0
    private val reportedUserId: Int = savedStateHandle["reportedUserId"] ?: 0

    fun onNextStep() = intent {
        if (state.step < TOTAL_STEPS) {
            reduce { state.copy(step = state.step + 1) }
        }
    }

    fun onPreviousStep() = intent {
        if (state.step > 1) {
            reduce { state.copy(step = state.step - 1) }
        }
    }

    fun onReasonSelect(reason: CallvanReportReason) = intent {
        reduce { state.copy(selectedReason = reason, isOtherReasonError = false) }
    }

    fun onOtherReasonChange(text: String) = intent {
        reduce { state.copy(otherReason = text, isOtherReasonError = false) }
    }

    fun onDetailChange(text: String) = intent {
        reduce { state.copy(detail = text) }
    }

    fun onAddImages(uris: List<Uri>) = intent {
        reduce { state.copy(images = (state.images + uris).toPersistentList()) }
    }

    fun onRemoveImage(index: Int) = intent {
        reduce { state.copy(images = state.images.toMutableList().also { it.removeAt(index) }.toPersistentList()) }
    }

    fun onSubmit() = intent {
        val reason = state.selectedReason ?: return@intent
        if (reason == CallvanReportReason.OTHER && state.otherReason.isBlank()) {
            reduce { state.copy(isOtherReasonError = true) }
            return@intent
        }
        val reasons = listOf(
            reason.name to if (reason == CallvanReportReason.OTHER) state.otherReason else null
        )
        reduce { state.copy(isLoading = true) }
        reportCallvanUserUseCase(
            postId = postId,
            reportedUserId = reportedUserId,
            reasons = reasons
        ).onSuccess {
            postSideEffect(CallvanReportSideEffect.NavigateBack)
        }.onFailure { throwable ->
            reduce { state.copy(isLoading = false) }
            val message = when (throwable) {
                is KoinCallvanException.CallvanReportSelfException -> "본인을 신고할 수 없습니다."
                is KoinCallvanException.CallvanReportAlreadyPendingException -> "이미 신고가 접수된 사용자입니다."
                is KoinCallvanException.CallvanReportOnlyParticipantException -> "같은 콜밴 참여자만 신고할 수 있습니다."
                else -> "신고에 실패했습니다. 다시 시도해주세요."
            }
            postSideEffect(CallvanReportSideEffect.ShowErrorMessage(message))
        }
    }

    companion object {
        const val TOTAL_STEPS = 2
    }
}
