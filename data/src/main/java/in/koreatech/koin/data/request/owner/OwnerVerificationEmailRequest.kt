package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerVerificationEmailRequest(
    @SerializedName("address") val address: String
)
