package `in`.koreatech.koin.domain.model.callvan

data class CallvanRestriction(
    val isRestricted: Boolean,
    val restrictionType: CallvanRestrictionType,
    val restrictedUntil: String?
) {
    sealed class CallvanRestrictionType(val value: String) {
        object TemporaryRestriction14Days : CallvanRestrictionType("TEMPORARY_RESTRICTION_14_DAYS")
        object NoRestriction : CallvanRestrictionType("")

        companion object {
            fun from(value: String?): CallvanRestrictionType = when (value) {
                TemporaryRestriction14Days.value -> TemporaryRestriction14Days
                else -> NoRestriction
            }
        }
    }
}
