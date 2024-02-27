package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class StudentInfoRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("gender")
    val gender: Int,

    @SerializedName("is_graduated")
    val isGraduated: Boolean,

    @SerializedName("major")
    val major: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("nickname")
    val nickName: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("student_number")
    val studentNumber: String
)