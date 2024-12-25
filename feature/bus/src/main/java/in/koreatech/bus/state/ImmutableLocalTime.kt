package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import java.time.LocalTime

@Immutable
data class ImmutableLocalTime(
    val localTime: LocalTime
)
