package `in`.koreatech.koin.feature.callvan.ui.detail

import `in`.koreatech.koin.feature.callvan.ui.detail.model.CallvanDetailParticipantUiItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanDetailState(
    val departure: String = "",
    val destination: String = "",
    val dateTime: String = "",
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val participants: ImmutableList<CallvanDetailParticipantUiItem> = persistentListOf(),
    val hasNewNotification: Boolean = false,
    val isLoading: Boolean = false
)