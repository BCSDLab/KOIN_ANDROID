package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class UserSemestersResponse(
    @SerializedName("semesters")
    val semesters: List<SemesterResponseV3>
)