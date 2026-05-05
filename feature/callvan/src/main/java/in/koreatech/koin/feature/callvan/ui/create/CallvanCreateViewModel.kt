package `in`.koreatech.koin.feature.callvan.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException
import `in`.koreatech.koin.domain.usecase.callvan.CreateCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanRestrictionUseCase
import `in`.koreatech.koin.feature.callvan.model.toUiState
import `in`.koreatech.koin.feature.callvan.MAX_PARTICIPANTS_COUNT
import `in`.koreatech.koin.feature.callvan.MIN_PARTICIPANTS_COUNT
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import `in`.koreatech.koin.feature.callvan.ui.create.model.SubmitErrorType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@Suppress("TooManyFunctions")
@HiltViewModel
class CallvanCreateViewModel @Inject constructor(
    private val createCallvanPostUseCase: CreateCallvanPostUseCase,
    private val getCallvanRestrictionUseCase: GetCallvanRestrictionUseCase
) : ViewModel(), ContainerHost<CallvanCreateState, CallvanCreateSideEffect> {

    override val container: Container<CallvanCreateState, CallvanCreateSideEffect> = container(
        CallvanCreateState()
    )

    fun openDepartureLocationPicker() = blockingIntent {
        reduce { state.copy(isLocationPickerVisible = true, isPickingDeparture = true) }
    }

    fun openArrivalLocationPicker() = blockingIntent {
        reduce { state.copy(isLocationPickerVisible = true, isPickingDeparture = false) }
    }

    fun closeLocationPicker() = blockingIntent {
        reduce { state.copy(isLocationPickerVisible = false) }
    }

    fun selectLocation(location: CallvanLocationOption, customText: String? = null) = blockingIntent {
        reduce {
            if (state.isPickingDeparture) {
                state.copy(
                    departureLocation = location,
                    departureCustomText = customText,
                    isLocationPickerVisible = false
                )
            } else {
                state.copy(
                    arrivalLocation = location,
                    arrivalCustomText = customText,
                    isLocationPickerVisible = false
                )
            }
        }
    }

    fun swapLocations() = blockingIntent {
        reduce {
            state.copy(
                departureLocation = state.arrivalLocation,
                arrivalLocation = state.departureLocation,
                departureCustomText = state.arrivalCustomText,
                arrivalCustomText = state.departureCustomText
            )
        }
    }

    fun toggleDatePicker() = blockingIntent {
        reduce {
            state.copy(
                isDatePickerVisible = !state.isDatePickerVisible,
                isTimePickerVisible = false
            )
        }
    }

    fun updateDate(date: LocalDate) = blockingIntent {
        val today = LocalDate.now()
        val clampedDate = if (date.isBefore(today)) today else date
        val clampedTime = if (clampedDate == today) maxOf(state.selectedTime, LocalTime.now()) else state.selectedTime
        reduce { state.copy(selectedDate = clampedDate, selectedTime = clampedTime) }
    }

    fun resetDate() = blockingIntent {
        reduce { state.copy(selectedDate = LocalDate.now()) }
    }

    fun confirmDate() = blockingIntent {
        reduce { state.copy(isDatePickerVisible = false) }
    }

    fun toggleTimePicker() = blockingIntent {
        reduce {
            state.copy(
                isTimePickerVisible = !state.isTimePickerVisible,
                isDatePickerVisible = false
            )
        }
    }

    fun updateTime(time: LocalTime) = blockingIntent {
        val clamped = if (state.selectedDate == LocalDate.now()) maxOf(time, LocalTime.now()) else time
        reduce { state.copy(selectedTime = clamped) }
    }

    fun resetTime() = blockingIntent {
        reduce { state.copy(selectedTime = LocalTime.now()) }
    }

    fun confirmTime() = blockingIntent {
        reduce { state.copy(isTimePickerVisible = false) }
    }

    fun decrementParticipants() = blockingIntent {
        reduce {
            if (state.maxParticipants > MIN_PARTICIPANTS_COUNT) {
                state.copy(maxParticipants = state.maxParticipants - 1)
            } else {
                state
            }
        }
    }

    fun incrementParticipants() = blockingIntent {
        reduce {
            if (state.maxParticipants < MAX_PARTICIPANTS_COUNT) {
                state.copy(maxParticipants = state.maxParticipants + 1)
            } else {
                state
            }
        }
    }

    fun updateBanDialogVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(showBanDialog = visible) }
    }

    fun fetchRestriction() = intent {
        getCallvanRestrictionUseCase()
            .onSuccess { restriction ->
                val uiState = restriction.toUiState()
                reduce { state.copy(restriction = uiState, showBanDialog = uiState.isRestricted) }
            }
    }

    private fun isDepartureInPast(state: CallvanCreateState): Boolean {
        val selected = LocalDateTime.of(state.selectedDate, state.selectedTime)
        return selected.isBefore(LocalDateTime.now())
    }

    @Suppress("CognitiveComplexMethod")
    fun submit() = intent {
        val currentState = state
        if (!currentState.isFormComplete || currentState.isSubmitting) return@intent
        if (currentState.departureLocation == null || currentState.arrivalLocation == null) return@intent

        if (currentState.restriction.isRestricted) {
            reduce { state.copy(showBanDialog = true) }
            return@intent
        }

        /* 현재 날짜 이후 검증 로직 */
        if (isDepartureInPast(currentState)) {
            val now = LocalDateTime.now()
            reduce { state.copy(selectedDate = now.toLocalDate(), selectedTime = now.toLocalTime()) }
            postSideEffect(CallvanCreateSideEffect.ShowPastTimeError)
            return@intent
        }

        reduce { state.copy(isSubmitting = true) }
        createCallvanPostUseCase(
            departureType = currentState.departureLocation.name,
            departureCustomName = if (currentState.departureLocation == CallvanLocationOption.CUSTOM) {
                currentState.departureCustomText
            } else {
                null
            },
            arrivalType = currentState.arrivalLocation.name,
            arrivalCustomName = if (currentState.arrivalLocation == CallvanLocationOption.CUSTOM) {
                currentState.arrivalCustomText
            } else {
                null
            },
            departureDate = currentState.apiDepartureDate,
            departureTime = currentState.apiDepartureTime,
            maxParticipants = currentState.maxParticipants
        ).onSuccess {
            reduce { state.copy(isSubmitting = false) }
            postSideEffect(CallvanCreateSideEffect.NavigateToMain)
        }.onFailure { error ->
            reduce { state.copy(isSubmitting = false) }
            val errorType = when (error) {
                is KoinCallvanException.InvalidRequestBodyException -> SubmitErrorType.INVALID_REQUEST_BODY
                is KoinCallvanException.InvalidCustomLocationNameException -> SubmitErrorType.INVALID_CUSTOM_LOCATION_NAME
                is KoinCallvanException.NotFoundUserException -> SubmitErrorType.NOT_FOUND_USER
                else -> SubmitErrorType.UNKNOWN
            }
            postSideEffect(CallvanCreateSideEffect.ShowSubmitError(errorType))
        }
    }
}