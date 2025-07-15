package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class GeneralUserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login_id") val loginId: String,
    @SerializedName("email") val email: String?,
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("gender") val gender: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("user_type") val userType: String
)
