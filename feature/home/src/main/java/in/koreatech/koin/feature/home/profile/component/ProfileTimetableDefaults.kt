package `in`.koreatech.koin.feature.home.profile.component

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.feature.home.profile.model.ProfileTimetableColor

internal object ProfileTimetableDefaults {
    const val START_HOUR = 9
    const val END_HOUR = 18
    val days = listOf("월", "화", "수", "목", "금")

    val gridLineColor = Color(0xFFF5F5F5)
    val labelColor = Color(0xFFB0B0B0)

    val colors = listOf(
        ProfileTimetableColor(Color(0xFF890000), Color(0xFFE7CCCC)),
        ProfileTimetableColor(Color(0xFFFF4444), Color(0xFFFFDADA)),
        ProfileTimetableColor(Color(0xFFFF993B), Color(0xFFFFEBD8)),
        ProfileTimetableColor(Color(0xFFE8D52A), Color(0xFFFAF7D4)),
        ProfileTimetableColor(Color(0xFFD0AE00), Color(0xFFF6EFCC)),
        ProfileTimetableColor(Color(0xFF513A00), Color(0xFFDCD8CC)),
        ProfileTimetableColor(Color(0xFF0C9D61), Color(0xFFCEEBDF)),
        ProfileTimetableColor(Color(0xFF7ABA78), Color(0xFFE4F1E4)),
        ProfileTimetableColor(Color(0xFF366718), Color(0xFFD7E1D1)),
        ProfileTimetableColor(Color(0xFF80C4E9), Color(0xFFE6F3FB)),
        ProfileTimetableColor(Color(0xFF1679AB), Color(0xFFD0E4EE)),
        ProfileTimetableColor(Color(0xFF074173), Color(0xFFCDD9E3)),
        ProfileTimetableColor(Color(0xFF523AE2), Color(0xFFDCD8F9)),
        ProfileTimetableColor(Color(0xFF6F6F6F), Color(0xFFE2E2E2)),
        ProfileTimetableColor(Color(0xFFCBCBCB), Color(0xFFF5F5F5))
    )
}
