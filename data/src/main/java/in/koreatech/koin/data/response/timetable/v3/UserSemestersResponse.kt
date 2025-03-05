package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.SemesterV3

data class UserSemestersResponse(
    @SerializedName("semesters")
    val semesters: List<SemesterResponseV3>
)

internal fun UserSemestersResponse.toSemesters(): List<SemesterV3>
        = this.semesters.map { it.toSemester() }

// TODO::마이그레이션 완료 후 제거
internal fun UserSemestersResponse.toLegacySemesters(): List<String> = this.semesters.map {it.toLegacySemesterString() }