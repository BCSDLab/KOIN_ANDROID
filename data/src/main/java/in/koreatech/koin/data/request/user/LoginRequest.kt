package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.constant.URLConstant

data class LoginRequest(
    @SerializedName(URLConstant.USER.LOGIN_ID)
    val loginId: String,
    @SerializedName(URLConstant.USER.LOGIN_PW)
    val loginPw: String
)
