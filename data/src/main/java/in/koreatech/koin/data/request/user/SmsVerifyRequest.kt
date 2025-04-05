package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class SmsVerifyRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("certification_code")
    val certificationCode: String
)