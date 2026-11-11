package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class StudentAcademicInfoRequest(
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("department") val department: String,
    @SerializedName("major") val major: String?
)
