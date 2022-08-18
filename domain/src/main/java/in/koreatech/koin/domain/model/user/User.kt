package `in`.koreatech.koin.domain.model.user

data class User(
    val credentialsNonExpired: Boolean,
    val isGraduated: Boolean,
    val enabled: Boolean,
    val anonymousNickname: String,
    val portalAccount: String,
    val identity: Int,
    val name: String,
    val nickname: String,
    val accountNonExpired: String,
    val id: String,
    val accountNonLocked: String,
    val username: String
)