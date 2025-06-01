package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class StudentUserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login_id") val loginId: String,
    @SerializedName("email") val email: String?,
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("gender") val gender: Int?, // Old user does not have this field
    @SerializedName("major") val major: String?, // Old user does not have this field
    @SerializedName("name") val name: String?, // Old user does not have this field
    @SerializedName("nickname") val nickname: String?, // Old user does not have this field
    @SerializedName("phone_number") val phoneNumber: String?, // Old user does not have this field
    @SerializedName("student_number") val studentNumber: String?, // Old user does not have this field
    @SerializedName("user_type") val userType: String
)
