package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerVerificationEmailRequest(
    @SerializedName("address") val address: String
)

data class VerificationSmsRequest(
    @SerializedName("phone_number") val phoneNumber: String?
)

data class CheckExistsAccount(
    @SerializedName("account") val phoneNumber: String?,
)