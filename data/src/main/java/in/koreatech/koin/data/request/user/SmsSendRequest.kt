package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class SmsSendRequest(
    @SerializedName("target")
    val target: String
)
