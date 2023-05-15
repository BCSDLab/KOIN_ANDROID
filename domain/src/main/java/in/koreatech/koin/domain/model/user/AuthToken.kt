package `in`.koreatech.koin.domain.model.user

data class AuthToken(
    val accessToken: String,
    val refreshToken: String?
)
