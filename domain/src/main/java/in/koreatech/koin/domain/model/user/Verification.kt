package `in`.koreatech.koin.domain.model.user

sealed class Verification {
    data object OK : Verification()
    data object INVALID : Verification()
    data object NOCODE : Verification()
    data object UNDEFINED : Verification()
}
