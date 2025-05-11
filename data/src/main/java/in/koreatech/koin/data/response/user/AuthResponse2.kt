package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class AuthResponse2(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user_type") val userType: String
)
