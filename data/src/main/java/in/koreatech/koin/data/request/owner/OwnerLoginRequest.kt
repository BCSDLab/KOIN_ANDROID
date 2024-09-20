package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerLoginRequest(
    @SerializedName("account") val phoneNumber: String,
    @SerializedName("password") val password: String,
)