package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.Semester

data class UserSemestersResponse(
    @SerializedName("semesters")
    val semesters: List<SemesterResponse>
)

internal fun UserSemestersResponse.toSemesters(): List<Semester> = this.semesters.map { it.toSemester() }
