package `in`.koreatech.koin.domain.model.user

data class AuthToken(
    val token: String,
    val user: User
)
