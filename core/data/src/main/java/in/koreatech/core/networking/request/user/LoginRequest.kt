package `in`.koreatech.core.networking.request.user

import `in`.koreatech.koin.data.constant.URLConstant
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName(URLConstant.USER.ID)
    val portalAccount: String,

    @SerializedName(URLConstant.USER.PW)
    val passwordHashed: String
)
