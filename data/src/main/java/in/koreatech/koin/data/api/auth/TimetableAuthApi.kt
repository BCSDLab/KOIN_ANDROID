package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLecturesQueryRequest
import `in`.koreatech.koin.data.response.timetable.SemesterCheckResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFrameResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFramesResponse
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
        @Query("timetable_frame_id") timetableFrameId: Int,
    ): TimetableLecturesResponse

    @PUT("/v2/timetables/lecture")
    suspend fun putTimetableLectures(
        @Body lectures: TimetableLecturesQueryRequest,
    ): TimetableLecturesResponse

    @POST("/v2/timetables/lecture")
    suspend fun postTimetableLectures(
        @Body lectures: LecturesQueryRequest,
    ): TimetableLecturesResponse

    @PUT("/v2/timetables/frame/{id}")
    suspend fun putTimetableFrame(
        @Path("id") id: Int,
        @Body frame: TimetableFrameQueryRequest,
    ): TimetableFrameResponse

    @POST("/v2/timetables/frame")
    suspend fun postTimetableFrame(
        @Body frame: TimetableFrameCreateQueryRequest,
    ): TimetableFrameResponse

    @POST("/v2/timetables/frame/rollback")
    suspend fun postRollbackFrame(
        @Query("timetable_frame_id") frameId: Int,
    ): TimetableLecturesResponse

    @DELETE("/v2/timetables/frame")
    suspend fun deleteTimetableFrame(
        @Query("id") frameId: Int,
    ): Response<Unit>

    /**
     * @param semester 학기명
     * @return 학기의 프레임 리스트
     */
    @GET("/v2/timetables/frames")
    suspend fun getTimetableFrames(
        @Query("semester") semester: String,
    ): List<TimetableFrameResponse>

    /**
     * 학생이 추가한 모든 학기의 프레임을 불러옴
     * @return 학생이 추가한 모든 시간표 프레임
     */
    @GET("/v2/timetables/frames")
    suspend fun getAllFrames(): TimetableFramesResponse

    @DELETE("/v2/timetables/lecture/{id}")
    suspend fun deleteTimetableLecture(
        @Path("id") id: Int,
    ): Response<Unit>

    @DELETE("/v2/timetables/frame/{frameId}/lecture/{lectureId}")
    suspend fun deleteTimetableFrameLecture(
        @Path("frameId") frameId: Int,
        @Path("lectureId") lectureId: Int,
    ): Response<Unit>

    @DELETE("/v2/timetables/lectures")
    suspend fun deleteTimetableLectures(
        @Query("timetable_lecture_ids") lectureIds: List<Int>,
    ): Response<Unit>

    @DELETE("/v2/all/timetables/frame")
    suspend fun deleteAllTimetableFrame(
        @Query("semester") semester: String,
    ): Response<Unit>
}
