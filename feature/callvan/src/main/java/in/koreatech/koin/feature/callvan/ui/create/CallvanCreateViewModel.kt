package `in`.koreatech.koin.feature.callvan.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.callvan.CreateCallvanPostUseCase
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import java.util.Calendar
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

    fun updateYear(yearIndex: Int) = blockingIntent {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val newYear = currentYear + yearIndex
        reduce {
            val maxDay = getDaysInMonth(newYear, state.selectedMonth)
            state.copy(
                selectedYear = newYear,
                selectedDay = state.selectedDay.coerceAtMost(maxDay)
            )
        }
    }

    fun updateMonth(monthIndex: Int) = blockingIntent {
        val newMonth = monthIndex + 1
        reduce {
            val maxDay = getDaysInMonth(state.selectedYear, newMonth)
            state.copy(
                selectedMonth = newMonth,
                selectedDay = state.selectedDay.coerceAtMost(maxDay)
            )
        }
    }

    fun updateDay(dayIndex: Int) = blockingIntent {
        reduce { state.copy(selectedDay = dayIndex + 1) }
    }

    fun resetDate() = blockingIntent {
        val today = Calendar.getInstance()
        reduce {
            state.copy(
                selectedYear = today.get(Calendar.YEAR),
                selectedMonth = today.get(Calendar.MONTH) + 1,
                selectedDay = today.get(Calendar.DAY_OF_MONTH)
            )
        }
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
        reduce { state.copy(isSubmitting = true) }
        createCallvanPostUseCase(
            departureType = if (currentState.departureLocation == CallvanLocationOption.OTHER) {
                currentState.departureCustomText ?: ""
            } else {
                currentState.departureLocation!!.type
            },
            arrivalType = if (currentState.arrivalLocation == CallvanLocationOption.OTHER) {
                currentState.arrivalCustomText ?: ""
            } else {
                currentState.arrivalLocation!!.type
            },
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

    private fun getDaysInMonth(year: Int, month: Int): Int {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}
