package `in`.koreatech.core.networking.response.user

import com.google.gson.annotations.SerializedName

data class UserInfoEditResponse(
    @SerializedName("success") val success: String,
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String
)
