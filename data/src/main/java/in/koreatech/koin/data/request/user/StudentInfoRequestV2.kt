package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class StudentInfoRequestV2(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("student_number")
    val studentNumber: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String
)
