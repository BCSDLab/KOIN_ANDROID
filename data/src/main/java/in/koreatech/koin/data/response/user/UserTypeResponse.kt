package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class UserTypeResponse(
    @SerializedName("user_type") val userType: String,
)
