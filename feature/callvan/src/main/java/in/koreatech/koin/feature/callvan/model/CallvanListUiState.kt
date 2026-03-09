package `in`.koreatech.koin.feature.callvan.model

import android.os.Parcelable
import `in`.koreatech.koin.feature.callvan.enums.CallvanRouteState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallvanListUiState(
    val id: Int,
    val departure: String,
    val destination: String,
    val date: String,
    val time: String,
    val currentCount: Int,
    val maxCount: Int,
    val routeState: CallvanRouteState
) : Parcelable
