package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class CheckPhoneExistsRequest(
    @SerializedName("phone_number") val phoneNumber: String
)
