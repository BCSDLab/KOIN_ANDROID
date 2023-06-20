package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("token") val accessToken: String
)
