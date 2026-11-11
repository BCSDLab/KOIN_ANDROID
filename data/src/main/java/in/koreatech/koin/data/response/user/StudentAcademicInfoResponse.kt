package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class StudentAcademicInfoResponse(
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("major") val major: String?
)
