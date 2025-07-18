package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.club.ClubEvent
import `in`.koreatech.koin.feature.club.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubEvent(
    val id: Int,
    val name: String,
    val imageUrls: List<String>,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val introduce: String,
    val content: String,
    val status: EventStatus,
    val isSubscribed: Boolean
) : Parcelable

fun ClubEvent.toParcelizeClubEvent(): ParcelizeClubEvent {
    return ParcelizeClubEvent(
        id = id,
        name = name,
        imageUrls = imageUrls,
        startDateTime = startDate.toLocalDateTime(),
        endDateTime = endDate.toLocalDateTime(),
        introduce = introduce,
        content = content ?: "",
        status = status.toEventStatus(),
        isSubscribed = isSubscribed ?: false
    )
}

enum class EventStatus(
    @StringRes val strRes: Int
) {
    UNKNOWN(R.string.club_event_unknown),
    SOON(R.string.club_event_soon),
    ONGOING(R.string.club_event_ongoing),
    UPCOMING(R.string.club_event_upcoming),
    ENDED(R.string.club_event_ended)
}

fun String.toEventStatus() = when (this) {
    "SOON" -> EventStatus.SOON
    "ONGOING" -> EventStatus.ONGOING
    "UPCOMING" -> EventStatus.UPCOMING
    "ENDED" -> EventStatus.ENDED
    else -> EventStatus.UNKNOWN
}

fun String.toLocalDateTime(): LocalDateTime {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return LocalDateTime.parse(this, dateTimeFormatter)
}

fun LocalDateTime.toStringForm(): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'.' HH:mm")
    return this.format(dateTimeFormatter)
}
