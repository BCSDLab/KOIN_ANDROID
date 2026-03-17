package `in`.koreatech.koin.feature.callvan.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.callvan.CreateCallvanPostUseCase
import `in`.koreatech.koin.feature.callvan.ui.create.model.CallvanLocationOption
import java.time.LocalDate
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
    private val createCallvanPostUseCase: CreateCallvanPostUseCase
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
        val clamped = if (date.isBefore(today)) today else date
        reduce { state.copy(selectedDate = clamped) }
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

    fun updateAmPm(amPmIndex: Int) = blockingIntent {
        reduce { state.copy(isAm = amPmIndex == 0) }
    }

    fun updateHour(hourIndex: Int) = blockingIntent {
        reduce { state.copy(selectedHour = hourIndex + 1) }
    }

    fun updateMinute(minuteIndex: Int) = blockingIntent {
        reduce { state.copy(selectedMinute = minuteIndex) }
    }

    fun resetTime() = blockingIntent {
        reduce { state.copy(selectedHour = 12, selectedMinute = 0, isAm = true) }
    }

    fun confirmTime() = blockingIntent {
        reduce { state.copy(isTimePickerVisible = false) }
    }

    fun decrementParticipants() = blockingIntent {
        reduce {
            if (state.maxParticipants > 1) {
                state.copy(maxParticipants = state.maxParticipants - 1)
            } else {
                state
            }
        }
    }

    fun incrementParticipants() = blockingIntent {
        reduce {
            if (state.maxParticipants < 8) {
                state.copy(maxParticipants = state.maxParticipants + 1)
            } else {
                state
            }
        }
    }

    fun submit() = intent {
        val currentState = state
        if (!currentState.isFormComplete || currentState.isSubmitting) return@intent
        if(currentState.departureLocation == null || currentState.arrivalLocation == null) return@intent
        reduce { state.copy(isSubmitting = true) }
        createCallvanPostUseCase(
            departureType = currentState.departureLocation.name,
            departureCustomName = if (currentState.departureLocation == CallvanLocationOption.CUSTOM) {
                currentState.departureCustomText
            } else null,
            arrivalType = currentState.arrivalLocation.name,
            arrivalCustomName = if (currentState.arrivalLocation == CallvanLocationOption.CUSTOM) {
                currentState.arrivalCustomText
            } else null,
            departureDate = currentState.apiDepartureDate,
            departureTime = currentState.apiDepartureTime,
            maxParticipants = currentState.maxParticipants
        ).onSuccess {
            reduce { state.copy(isSubmitting = false) }
            postSideEffect(CallvanCreateSideEffect.NavigateToMain)
        }.onFailure {
            reduce { state.copy(isSubmitting = false) }
            postSideEffect(CallvanCreateSideEffect.ShowSubmitError)
        }
    }

}
