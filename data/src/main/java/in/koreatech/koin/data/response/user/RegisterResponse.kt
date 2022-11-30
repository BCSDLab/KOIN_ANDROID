package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success") val success: String
)