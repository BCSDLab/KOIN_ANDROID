package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.constant.URLConstant

data class StudentInfoResponse(
    @SerializedName("email")
    val email: String,

    @SerializedName("gender")
    val gender: Int,

    @SerializedName("is_graduated")
    val isGraduated: Boolean,

    @SerializedName("name")
    val name: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("student_number")
    val studentNumber: String,
)
