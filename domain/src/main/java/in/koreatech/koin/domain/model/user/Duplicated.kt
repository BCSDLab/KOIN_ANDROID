package `in`.koreatech.koin.domain.model.user

sealed class Duplicated {
    object OK : Duplicated()
    object INVALID_FORMAT : Duplicated()
    object CONFLICT : Duplicated()
    object UNDEFINED : Duplicated()
}