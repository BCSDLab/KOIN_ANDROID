package `in`.koreatech.koin.feature.club.ui.clubeventcreate

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.EventStatus
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubEventCreateState(
    val isLoading: Boolean = false,
    val clubId: Int = -1,
    val eventImageUrls: List<String> = persistentListOf(),
    val eventStartDateTime: LocalDateTime = LocalDateTime.now().withMinute(0).withSecond(0),
    val eventEndDateTime: LocalDateTime = eventStartDateTime.plusDays(1),
    val eventStatus: EventStatus = EventStatus.SOON,
    val isEventSubscribed: Boolean = false,
    val showCreateCancelDialog: Boolean = false,
    val showCreateRequestDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val showTimePickerDialog: Boolean = false
) : Parcelable
