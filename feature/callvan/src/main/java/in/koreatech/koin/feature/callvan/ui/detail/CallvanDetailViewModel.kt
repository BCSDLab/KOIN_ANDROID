package `in`.koreatech.koin.feature.callvan.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostDetailUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import `in`.koreatech.koin.feature.callvan.ui.detail.model.toUiItem
import `in`.koreatech.koin.feature.callvan.util.formatDateTime
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CallvanDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCallvanPostDetailUseCase: GetCallvanPostDetailUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel(), ContainerHost<CallvanDetailState, CallvanDetailSideEffect> {

    override val container = container<CallvanDetailState, CallvanDetailSideEffect>(
        CallvanDetailState()
    )

    private val postId: Int = checkNotNull(savedStateHandle[KEY_POST_ID])

    init {
        fetchPostDetail()
    }

    companion object {
        private const val KEY_POST_ID = "postId"
        const val KEY_REPORT_SUCCESS = "reportSuccess"
    }

    internal fun fetchPostDetail() = intent {
        reduce { state.copy(isLoading = true) }
        getCallvanPostDetailUseCase(postId)
            .onSuccess { detail ->
                reduce {
                    state.copy(
                        departure = detail.departure,
                        destination = detail.arrival,
                        dateTime = formatDateTime(detail.departureDate, detail.departureTime),
                        currentParticipants = detail.currentParticipants,
                        maxParticipants = detail.maxParticipants,
                        participants = detail.participants.sortedByDescending { it.isMe }.map { it.toUiItem() }.toPersistentList(),
                        isLoading = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
            }
    }

    internal fun fetchHasNewNotification() = intent {
        getNotificationsUseCase()
            .onSuccess { notifications ->
                reduce {
                    state.copy(hasNewNotification = notifications.any { !it.isRead })
                }
            }
    }
}
