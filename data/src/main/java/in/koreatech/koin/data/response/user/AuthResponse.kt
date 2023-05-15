package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token") val accessToken: String,
    @SerializedName("user_type") val userType: String,
    @SerializedName("refresh_token") val refreshToken: String
)
