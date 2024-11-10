package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLecturesQueryRequest
import `in`.koreatech.koin.data.response.timetable.SemesterCheckResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFrameResponse
import `in`.koreatech.koin.data.response.timetable.TimetableLecturesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TimetableAuthApi {
    @GET("/semesters/check")
    suspend fun getSemestersCheck(): SemesterCheckResponse

    @GET("/v2/timetables/lecture")
    suspend fun getTimetableLectures(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponse

    @PUT("/v2/timetables/lecture")
    suspend fun putTimetableLectures(
        @Body lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse

    @POST("/v2/timetables/lecture")
    suspend fun postTimetableLectures(
        @Body lectures: LecturesQueryRequest
    ): TimetableLecturesResponse

    @PUT("/v2/timetables/frame/{id}")
    suspend fun putTimetableFrame(
        @Path("id") id: Int,
        @Body frame: TimetableFrameQueryRequest
    ): TimetableFrameResponse

    @POST("/v2/timetables/frame")
    suspend fun postTimetableFrame(
        @Body frame: TimetableFrameCreateQueryRequest
    ): TimetableFrameResponse

    @DELETE("/v2/timetables/frame")
    suspend fun deleteTimetableFrame()

    @GET("/v2/timetables/frames")
    suspend fun getTimetableFrames(
        @Query("semester") semester: String
    ): List<TimetableFrameResponse>

    @DELETE("/v2/timetables/lecture/{id}")
    suspend fun deleteTimetableLecture(
        @Path("id") id: Int
    )

    @DELETE("/v2/timetables/frame/{frameId}/lecture/{lectureId}")
    suspend fun deleteTimetableFrameLecture(
        @Path("frameId") frameId: Int,
        @Path("lectureId") lectureId: Int
    ): Response<Unit>

    @DELETE("/v2/timetables/lectures")
    suspend fun deleteTimetableLectures(
        @Query("timetable_lecture_ids") lectureIds : List<Int>
    ): Response<Unit>

    @DELETE("/v2/all/timetables/frame")
    suspend fun deleteAllTimetableFrame()
}