package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class StudentRequest(
    @SerializedName("gender") val gender: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("major") val major: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
)
