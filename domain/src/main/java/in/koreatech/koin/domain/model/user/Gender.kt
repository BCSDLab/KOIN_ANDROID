package `in`.koreatech.koin.domain.model.user

sealed class Gender {
    object Man: Gender()
    object Woman: Gender()
    object Unknown: Gender()
}
