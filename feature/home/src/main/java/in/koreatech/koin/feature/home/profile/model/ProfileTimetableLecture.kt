package `in`.koreatech.koin.feature.home.profile.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ProfileTimetableLecture(
    val name: String,
    val place: String,
    val dayOfWeek: Int,
    val startTotalMinutes: Int,
    val endTotalMinutes: Int,
    val colorIndex: Int
)

data class ProfileTimetableColor(
    val header: Color,
    val content: Color
)
