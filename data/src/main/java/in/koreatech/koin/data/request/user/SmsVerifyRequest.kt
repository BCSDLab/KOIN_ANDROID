package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class SmsVerifyRequest(
    @SerializedName("target")
    val target: String,
    @SerializedName("code")
    val code: String
)
