package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequest(
    @SerializedName("device_token")
    val deviceToken: String,
)