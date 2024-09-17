package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerAuthResponse (
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String
)
