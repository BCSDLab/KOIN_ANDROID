package `in`.koreatech.koin.data.response

data class AuthResponse(
    val token: String,
    val user: UserResponse,
    val ttl: Int
)
