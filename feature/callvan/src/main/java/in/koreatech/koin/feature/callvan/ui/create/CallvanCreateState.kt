package `in`.koreatech.koin.feature.callvan.ui.create

import `in`.koreatech.koin.feature.callvan.ui.create.model.CallvanLocationOption
import java.time.LocalDate

data class CallvanCreateState(
    val departureLocation: CallvanLocationOption? = null,
    val arrivalLocation: CallvanLocationOption? = null,
    val departureCustomText: String? = null,
    val arrivalCustomText: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedHour: Int = 12,
    val selectedMinute: Int = 0,
    val isAm: Boolean = true,
    val maxParticipants: Int = 1,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isLocationPickerVisible: Boolean = false,
    val isPickingDeparture: Boolean = true,
    val isSubmitting: Boolean = false
) {
    val isFormComplete: Boolean
        get() {
            val departureValid = departureLocation != null &&
                (departureLocation != CallvanLocationOption.CUSTOM || !departureCustomText.isNullOrBlank())
            val arrivalValid = arrivalLocation != null &&
                (arrivalLocation != CallvanLocationOption.CUSTOM || !arrivalCustomText.isNullOrBlank())
            return departureValid && arrivalValid
        }

    val formattedDate: String
        get() = "${selectedDate.year}년 ${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일"

    val formattedTime: String
        get() = "%02d:%02d".format(selectedHour, selectedMinute)

    val amPmText: String
        get() = if (isAm) "오전" else "오후"

    val participantsText: String
        get() = "$maxParticipants 명"

    val apiDepartureDate: String
        get() = selectedDate.toString()

    val apiDepartureTime: String
        get() {
            val hour24 = when {
                isAm && selectedHour == 12 -> 0
                isAm -> selectedHour
                !isAm && selectedHour == 12 -> 12
                else -> selectedHour + 12
            }
            return "%02d:%02d".format(hour24, selectedMinute)
        }
}
