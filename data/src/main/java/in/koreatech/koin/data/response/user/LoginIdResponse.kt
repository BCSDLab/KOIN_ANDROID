package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class LoginIdResponse(
    @SerializedName("login_id") val loginId: String
)
