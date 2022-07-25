package `in`.koreatech.koin.data.response.user

data class AuthResponse(
    val token: String,
    val user: UserResponse,
    val ttl: Int
)
