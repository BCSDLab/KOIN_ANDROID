package `in`.koreatech.koin.data.request.owner

import `in`.koreatech.koin.data.constant.URLConstant
import com.google.gson.annotations.SerializedName

data class OwnerLoginRequest(
    @SerializedName(URLConstant.OWNER.EMAIL)
    val email: String,

    @SerializedName(URLConstant.OWNER.PW)
    val passwordHashed: String
)
