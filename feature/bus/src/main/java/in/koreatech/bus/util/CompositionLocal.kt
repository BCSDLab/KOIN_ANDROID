package `in`.koreatech.bus.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import `in`.koreatech.bus.type.BusType

internal val LocalOnRefresh =
    staticCompositionLocalOf {
        {}
    }

internal val LocalSelectedTimetableTab =
    compositionLocalOf {
        BusType.SHUTTLE
    }
