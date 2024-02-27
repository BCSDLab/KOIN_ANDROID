package `in`.koreatech.koin.data.request.user

import `in`.koreatech.koin.data.constant.URLConstant
import com.google.gson.annotations.SerializedName

data class IdRequest(
    @SerializedName(URLConstant.USER.EMAIL) val email: String
)
