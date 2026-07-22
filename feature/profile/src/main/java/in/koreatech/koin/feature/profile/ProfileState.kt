package `in`.koreatech.koin.feature.profile

import `in`.koreatech.koin.feature.profile.model.ProfileTimetableLecture
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProfileState(
    val isLoggedIn: Boolean = false,
    val name: String = "",
    val studentNumber: String = "",
    val timetable: ImmutableList<ProfileTimetableLecture> = persistentListOf(),
    val isNewNotificationReceived: Boolean = false
)
