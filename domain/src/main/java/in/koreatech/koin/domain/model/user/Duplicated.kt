package `in`.koreatech.koin.domain.model.user

sealed class Duplicated {
    data object OK : Duplicated()
    data object INVALID : Duplicated()
    data object CONFLICT : Duplicated()
    data object UNDEFINED : Duplicated()
}
