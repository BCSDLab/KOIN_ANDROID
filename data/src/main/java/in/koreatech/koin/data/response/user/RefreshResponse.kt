package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String,
)
