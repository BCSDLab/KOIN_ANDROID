package `in`.koreatech.koin.domain.model.user

sealed class Gender {
    object Male: Gender()
    object Female: Gender()
    object Unknown: Gender()
}
