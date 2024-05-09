package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerChangePasswordRequest (
    @SerializedName("address") val address: String,
    @SerializedName("password") val password: String
)