package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class SemesterResponseV3(
    @SerializedName("year")
    val year: Int,
    @SerializedName("term")
    val term: String
)