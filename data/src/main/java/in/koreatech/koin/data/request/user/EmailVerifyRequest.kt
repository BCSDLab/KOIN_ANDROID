package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class EmailVerifyRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("verification_code")
    val verificationCode: String
)
