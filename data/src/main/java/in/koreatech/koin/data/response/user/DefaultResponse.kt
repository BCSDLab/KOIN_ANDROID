package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: String,
    @SerializedName("code") val code: String,
    @SerializedName("grantEdit") val isGrantEdit: Boolean
)
