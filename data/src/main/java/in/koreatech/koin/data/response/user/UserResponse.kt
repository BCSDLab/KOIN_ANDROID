package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("gender") val gender: Int,
    @SerializedName("major") val major: String?,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("student_number") val studentNumber: String?,
)