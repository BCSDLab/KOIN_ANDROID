package `in`.koreatech.koin.feature.recruitment.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class StableLocalDate(val value: LocalDate) {
    companion object {
        fun now(): StableLocalDate = StableLocalDate(LocalDate.now())
    }
}

fun LocalDate.toStable(): StableLocalDate = StableLocalDate(this)
