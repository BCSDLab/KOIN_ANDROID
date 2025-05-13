package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class StudentResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("login_id") val loginId: String?,
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("major") val major: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("user_type") val userType: String?
)
