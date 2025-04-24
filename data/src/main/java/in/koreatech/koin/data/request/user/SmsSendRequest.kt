package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class SmsSendRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)
