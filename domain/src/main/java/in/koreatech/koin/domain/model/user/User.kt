package `in`.koreatech.koin.domain.model.user

data class User(
    val id: Int,
    val isGraduated: Boolean,
    val enabled: Boolean,
    val anonymousNickname: String?,
    val portalAccount: String,
    val identity: UserIdentity,
    val name: String,
    val studentNumber: String?,
    val profileImageUrl: String?,
    val gender: Gender,
    val nickname: String?,
    val phoneNumber: String?,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean,
    val username: String,
    val major: String?
)