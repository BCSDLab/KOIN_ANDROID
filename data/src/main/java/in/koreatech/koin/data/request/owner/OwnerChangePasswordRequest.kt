package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerChangePasswordRequest (
    @SerializedName("address") val address: String,
    @SerializedName("password") val password: String
)

data class OwnerChangePasswordSmsRequest (
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("password") val password: String
)