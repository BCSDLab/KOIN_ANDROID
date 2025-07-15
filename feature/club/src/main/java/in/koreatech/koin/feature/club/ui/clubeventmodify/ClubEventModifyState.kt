package `in`.koreatech.koin.feature.club.ui.clubeventmodify

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.EventStatus
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubEventModifyState(
    val isLoading: Boolean = false,
    val isEventLoading: Boolean = false,
    val clubId: Int = -1,
    val eventId: Int = -1,
    val eventName: String= "",
    val eventIntroduce: String = "",
    val eventContent: String = "",
    val eventImageUrls: List<String> = persistentListOf(),
    val eventStartDateTime: LocalDateTime = LocalDateTime.now().withMinute(0).withSecond(0),
    val eventEndDateTime: LocalDateTime = eventStartDateTime.plusDays(1),
    val showModifyCancelDialog: Boolean = false,
    val showModifyRequestDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val showTimePickerDialog: Boolean = false
) : Parcelable
