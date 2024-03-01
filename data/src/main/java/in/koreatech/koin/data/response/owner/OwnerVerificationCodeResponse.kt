package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerVerificationCodeResponse(
    @SerializedName("token") val token: String
)