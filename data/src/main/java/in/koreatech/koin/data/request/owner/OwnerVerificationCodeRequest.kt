package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerVerificationCodeRequest(
    @SerializedName("address") val address: String,
    @SerializedName("certification_code") val certificationCode: String
)

data class VerificationCodeSmsRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("certification_code") val certificationCode: String
)
