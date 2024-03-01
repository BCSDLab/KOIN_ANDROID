package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerVerificationCodeRequest(
    @SerializedName("address") val address: String,
    @SerializedName("certification_code") val certificationCode: String
)