package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class DepartmentsResponse(
    @SerializedName("departments")
    val departments: List<DepartmentResponse>,
)

data class DepartmentResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)