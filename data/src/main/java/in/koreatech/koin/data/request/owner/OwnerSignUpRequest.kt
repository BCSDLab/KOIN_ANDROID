package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.constant.URLConstant

data class OwnerSignUpRequest(
    @SerializedName(URLConstant.OWNER.EMAIL)
    val email: String
)
