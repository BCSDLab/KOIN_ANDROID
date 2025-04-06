package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String
)
