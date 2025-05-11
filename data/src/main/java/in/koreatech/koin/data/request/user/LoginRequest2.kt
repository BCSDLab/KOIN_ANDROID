package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class LoginRequest2(
    @SerializedName("login_id")
    val loginId: String,
    @SerializedName("login_pw")
    val loginPw: String
)
