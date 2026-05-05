package `in`.koreatech.koin.domain.model.callvan

data class CallvanRestriction(
    val isRestricted: Boolean,
    val restrictionType: CallvanRestrictionType,
    val restrictedUntil: String?
) {
    sealed class CallvanRestrictionType(val value: String) {
        object TemporaryRestriction14Days : CallvanRestrictionType("TEMPORARY_RESTRICTION_14_DAYS")
        object PermanentRestriction : CallvanRestrictionType("PERMANENT_RESTRICTION")
        object NoRestriction : CallvanRestrictionType("")

        companion object {
            fun from(value: String?): CallvanRestrictionType = when (value) {
                TemporaryRestriction14Days.value -> TemporaryRestriction14Days
                PermanentRestriction.value -> PermanentRestriction
                else -> NoRestriction
            }
        }
    }
}
