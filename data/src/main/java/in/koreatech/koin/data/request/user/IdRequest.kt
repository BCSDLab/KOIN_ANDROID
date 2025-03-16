package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.constant.URLConstant

data class IdRequest(
    @SerializedName(URLConstant.USER.EMAIL) val email: String
)
