package `in`.koreatech.koin.domain.model.user

sealed class UserIdentity {
    object Student: UserIdentity()
    object Professor: UserIdentity()
    object Staff: UserIdentity()
    object Unknown: UserIdentity()
}
