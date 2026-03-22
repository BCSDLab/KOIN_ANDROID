package `in`.koreatech.koin.feature.callvan.ui.create

import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import java.time.LocalDate
import java.time.LocalTime

data class CallvanCreateState(
    val departureLocation: CallvanLocationOption? = null,
    val arrivalLocation: CallvanLocationOption? = null,
    val departureCustomText: String? = null,
    val arrivalCustomText: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
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

    val apiDepartureDate: String
        get() = selectedDate.toString()

    val apiDepartureTime: String
        get() = "%02d:%02d".format(selectedTime.hour, selectedTime.minute)
}