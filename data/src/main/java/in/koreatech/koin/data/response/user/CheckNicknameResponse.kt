package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class CheckNicknameResponse(
    @SerializedName("success") val success: String
)
