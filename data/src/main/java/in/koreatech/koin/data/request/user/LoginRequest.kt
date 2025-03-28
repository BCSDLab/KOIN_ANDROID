package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.constant.URLConstant

data class LoginRequest(
    @SerializedName(URLConstant.USER.EMAIL)
    val email: String,
    @SerializedName(URLConstant.USER.PW)
    val password: String
)
