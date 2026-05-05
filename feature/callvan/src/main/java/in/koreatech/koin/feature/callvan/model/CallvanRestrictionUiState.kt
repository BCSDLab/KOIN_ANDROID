package `in`.koreatech.koin.feature.callvan.model

import `in`.koreatech.koin.domain.model.callvan.CallvanRestriction
import java.time.LocalDate

data class CallvanRestrictionUiState(
    val isRestricted: Boolean,
    val restrictionType: RestrictionType,
    val restrictedUntil: LocalDate?
) {
    enum class RestrictionType(val days: Int?) {
        TEMPORARY_14_DAYS(14), PERMANENT(null), NONE(null)
    }
}

fun CallvanRestriction.toUiState() = CallvanRestrictionUiState(
    isRestricted = isRestricted,
    restrictionType = when (restrictionType) {
        CallvanRestriction.CallvanRestrictionType.TemporaryRestriction14Days -> CallvanRestrictionUiState.RestrictionType.TEMPORARY_14_DAYS
        CallvanRestriction.CallvanRestrictionType.PermanentRestriction -> CallvanRestrictionUiState.RestrictionType.PERMANENT
        CallvanRestriction.CallvanRestrictionType.NoRestriction -> CallvanRestrictionUiState.RestrictionType.NONE
    },
    restrictedUntil = restrictedUntil?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
)