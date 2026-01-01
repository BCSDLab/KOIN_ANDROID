package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest
import `in`.koreatech.koin.data.response.timetable.TimetableFrameResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFramesResponse
import `in`.koreatech.koin.data.response.timetable.TimetableLecturesResponse
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFrameResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFramesResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableLecturesResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.UserSemestersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TimetableAuthApi {
    @GET("/v3/semesters/check")
    suspend fun getUserSemesters(): UserSemestersResponse

    @Deprecated("use getLecturesByTimetableFrameId instead")
    @GET("/v2/timetables/lecture")
    suspend fun getTimetableLectures(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponse

    @GET("/v3/timetables/lecture")
    suspend fun getLecturesByFrameId(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponseV3

    @Deprecated("use editTimetableRegularLecture and editTimetableCustomLecture instead")
    @PUT("/v2/timetables/lecture")
    suspend fun putTimetableLectures(
        @Body lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse

    // 시간표에 담긴 정규 강의의 정보를 수정
    @PUT("v3/timetables/lecture/regular")
    suspend fun editTimetableRegularLecture(
        @Body regularLectureRequest: TimetableRegularLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 담긴 커스텀 강의의 정보를 수정
    @PUT("v3/timetables/lecture/custom")
    suspend fun editTimetableCustomLecture(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    @Deprecated("use addRegularLectureOnTimetable and addCustomLectureOnTimetable instead")
    @POST("/v2/timetables/lecture")
    suspend fun postTimetableLectures(
        @Body lectures: LecturesQueryRequest
    ): TimetableLecturesResponse

    // 시간표에 새로운 정규 강의 추가
    @POST("/v3/timetables/lecture/regular")
    suspend fun addRegularLectureOnTimetable(
        @Body regularLectureRequest: TimetableRegularLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 새로운 커스텀 강의 추가
    @POST("/v3/timetables/lecture/custom")
    suspend fun addCustomLectureOnTimetable(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    // 프레임 수정
    @Deprecated("use editFrame instead")
    @PUT("/v2/timetables/frame/{id}")
    suspend fun putTimetableFrame(
        @Path("id") id: Int,
        @Body frame: TimetableFrameQueryRequest
    ): TimetableFrameResponse

    @PUT("/v3/timetables/frame/{id}")
    suspend fun editFrame(
        @Path("id") frameId: Int,
        @Query("name") frameName: String,
        @Query("is_main") isMain: Boolean
    ): List<TimetableFrameResponseV3>

    // 프레임 생성
    @Deprecated("use createFrame instead")
    @POST("/v2/timetables/frame")
    suspend fun postTimetableFrame(
        @Body frame: TimetableFrameCreateQueryRequest
    ): TimetableFrameResponse

    @POST("/v3/timetables/frame")
    suspend fun createFrame(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<TimetableFrameResponseV3>

    @Deprecated("use restoreFrameByFrameId instead")
    @POST("/v2/timetables/frame/rollback")
    suspend fun postRollbackFrame(
        @Query("timetable_frame_id") frameId: Int
    ): TimetableLecturesResponse

    // 삭제한 시간표 복구
    @POST("/v3/timetables/frame/rollback")
    suspend fun restoreFrameByFrameId(
        @Query("timetable_frame_id") frameId: Int
    ): TimetableLecturesResponseV3

    // 학기의 모든 프레임 삭제
    @DELETE("/v3/timetables/frames")
    suspend fun deleteFramesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): Response<Unit>

    @DELETE("/v2/timetables/frame")
    suspend fun deleteTimetableFrame(
        @Query("id") frameId: Int
    ): Response<Unit>

    /**
     * @param semester 학기명
     * @return 학기의 프레임 리스트
     */
    @Deprecated("use getFramesBySemester instead")
    @GET("/v2/timetables/frames")
    suspend fun getTimetableFrames(
        @Query("semester") semester: String
    ): List<TimetableFrameResponse>

    // 학기에 있는 프레임들 조회
    @GET("/v3/timetables/frame")
    suspend fun getFramesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<TimetableFrameResponseV3>

    /**
     * 학생이 추가한 모든 학기의 프레임을 불러옴
     * @return 학생이 추가한 모든 시간표 프레임
     */
    @Deprecated("use getAllFramesV3 instead")
    @GET("/v2/timetables/frames")
    suspend fun getAllFrames(): TimetableFramesResponse

    @GET("/v3/timetables/frames")
    suspend fun getAllFramesV3(): TimetableFramesResponseV3

    @DELETE("/v2/timetables/lecture/{id}")
    suspend fun deleteTimetableLecture(
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("/v2/timetables/frame/{frameId}/lecture/{lectureId}")
    suspend fun deleteTimetableFrameLecture(
        @Path("frameId") frameId: Int,
        @Path("lectureId") lectureId: Int
    ): Response<Unit>

    @DELETE("/v2/timetables/lectures")
    suspend fun deleteTimetableLectures(
        @Query("timetable_lecture_ids") lectureIds: List<Int>
    ): Response<Unit>

    @Deprecated("use deleteFramesBySemester instead")
    @DELETE("/v2/all/timetables/frame")
    suspend fun deleteAllTimetableFrame(
        @Query("semester") semester: String
    ): Response<Unit>
}
