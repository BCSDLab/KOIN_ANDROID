package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class TokenRsponse(
    @SerializedName("token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)
