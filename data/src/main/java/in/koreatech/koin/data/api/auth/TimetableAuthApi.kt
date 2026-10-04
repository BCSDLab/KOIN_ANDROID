package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameCreateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameUpdateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureCreateRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest
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

    @GET("/v3/timetables/lecture")
    suspend fun getLecturesByFrameId(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponseV3

    // 시간표에 담긴 정규 강의의 정보를 수정
    @PUT("/v3/timetables/lecture/regular")
    suspend fun editTimetableRegularLecture(
        @Body regularLectureRequest: TimetableRegularLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 담긴 커스텀 강의의 정보를 수정
    @PUT("/v3/timetables/lecture/custom")
    suspend fun editTimetableCustomLecture(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 새로운 정규 강의 추가
    @POST("/v3/timetables/lecture/regular")
    suspend fun addRegularLectureOnTimetable(
        @Body regularLectureRequest: TimetableRegularLectureCreateRequest
    ): TimetableLecturesResponseV3

    // 시간표에 새로운 커스텀 강의 추가
    @POST("/v3/timetables/lecture/custom")
    suspend fun addCustomLectureOnTimetable(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    // 프레임 수정
    @PUT("/v3/timetables/frame/{id}")
    suspend fun editFrame(
        @Path("id") frameId: Int,
        @Body frameUpdateRequest: TimetableFrameUpdateRequestV3
    ): List<TimetableFrameResponseV3>

    // 프레임 생성
    @POST("/v3/timetables/frame")
    suspend fun createFrame(
        @Body frameCreateRequest: TimetableFrameCreateRequestV3
    ): List<TimetableFrameResponseV3>

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
    @GET("/v3/timetables/frames")
    suspend fun getAllFramesV3(): List<TimetableFramesResponseV3>

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
}
