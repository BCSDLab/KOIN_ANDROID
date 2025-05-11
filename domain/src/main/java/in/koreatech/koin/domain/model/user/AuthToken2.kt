package `in`.koreatech.koin.domain.model.user

data class AuthToken2(
    val accessToken: String,
    val refreshToken: String,
    val userType: String
)
