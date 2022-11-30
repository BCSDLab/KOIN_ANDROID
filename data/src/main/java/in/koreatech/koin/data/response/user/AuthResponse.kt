package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("ttl") val ttl: Int
)
