package `in`.koreatech.koin.feature.callvan.ui.report

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.callvan.ReportCallvanUserUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CallvanReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reportCallvanUserUseCase: ReportCallvanUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel(), ContainerHost<CallvanReportState, CallvanReportSideEffect> {

    override val container = container<CallvanReportState, CallvanReportSideEffect>(
        CallvanReportState()
    )

    private val postId: Int = checkNotNull(savedStateHandle["postId"])
    private val reportedUserId: Int = checkNotNull(savedStateHandle["reportedUserId"])

    fun onNextStep() = intent {
        if (!validateFirstStep()) return@intent
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
        val updated = state.selectedReasons.toMutableList().also { list ->
            if (reason in list) list.remove(reason) else list.add(reason)
        }.toPersistentList()
        reduce { state.copy(selectedReasons = updated, isOtherReasonError = false) }
    }

    fun onOtherReasonChange(text: String) = intent {
        reduce { state.copy(otherReason = text, isOtherReasonError = false) }
    }

    fun onDetailChange(text: String) = intent {
        reduce { state.copy(detail = text) }
    }

    fun uploadImage(
        mediaName: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri
    ) = intent {
        reduce { state.copy(isLoading = true) }
        uploadImageUseCase(
            domain = PreSignedUrlDomain.CALLVAN_REPORT,
            contentLength = mediaSize,
            contentType = mediaType,
            fileName = mediaName,
            imageUri = imageUri.toString()
        ).onSuccess { uploadedUrl ->
            reduce {
                state.copy(
                    images = state.images.toPersistentList().add(uploadedUrl),
                    isLoading = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(CallvanReportSideEffect.ShowErrorMessage("이미지 업로드에 실패했습니다."))
        }
    }

    fun onRemoveImage(index: Int) = intent {
        if (index !in state.images.indices) return@intent
        reduce {
            state.copy(
                images = state.images.toMutableList().also { it.removeAt(index) }.toPersistentList()
            )
        }
    }

    fun onSubmit() = intent {
        if (state.isLoading) return@intent
        if (!validateSecondStep()) {
            onPreviousStep()
            return@intent
        }
        val reasons = state.selectedReasons.map { reason ->
            reason.name to if (reason == CallvanReportReason.OTHER) state.otherReason else null
        }
        reduce { state.copy(isLoading = true) }
        reportCallvanUserUseCase(
            postId = postId,
            reportedUserId = reportedUserId,
            description = state.detail.ifBlank { null },
            reasons = reasons,
            attachmentUrls = state.images.takeIf { it.isNotEmpty() }
        ).onSuccess {
            postSideEffect(CallvanReportSideEffect.SubmitSuccess)
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

    private suspend fun SimpleSyntax<CallvanReportState, CallvanReportSideEffect>.validateFirstStep(): Boolean {
        if (state.selectedReasons.isEmpty()) return false
        if (CallvanReportReason.OTHER in state.selectedReasons && state.otherReason.isBlank()) {
            reduce { state.copy(isOtherReasonError = true) }
            return false
        }
        return true
    }

    private suspend fun SimpleSyntax<CallvanReportState, CallvanReportSideEffect>.validateSecondStep(): Boolean {
        return validateFirstStep()
    }

    companion object {
        const val TOTAL_STEPS = 2
    }
}
