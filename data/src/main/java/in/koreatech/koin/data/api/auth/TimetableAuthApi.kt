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
    @GET(URLConstant.TIMETABLE.V3.SEMESTERS.CHECK)
    suspend fun getUserSemesters(): UserSemestersResponse

    @Deprecated("use getLecturesByTimetableFrameId instead")
    @GET(URLConstant.TIMETABLE.V2.LECTURE.LECTURE)
    suspend fun getTimetableLectures(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponse

    @GET(URLConstant.TIMETABLE.V3.LECTURE.LECTURE)
    suspend fun getLecturesByFrameId(
        @Query("timetable_frame_id") timetableFrameId: Int
    ): TimetableLecturesResponseV3

    @Deprecated("use editTimetableRegularLecture and editTimetableCustomLecture instead")
    @PUT(URLConstant.TIMETABLE.V2.LECTURE.LECTURE)
    suspend fun putTimetableLectures(
        @Body lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse

    // 시간표에 담긴 정규 강의의 정보를 수정
    @PUT(URLConstant.TIMETABLE.V3.LECTURE.REGULER)
    suspend fun editTimetableRegularLecture(
        @Body regularLectureRequest: TimetableRegularLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 담긴 커스텀 강의의 정보를 수정
    @PUT(URLConstant.TIMETABLE.V3.LECTURE.CUSTOM)
    suspend fun editTimetableCustomLecture(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    @Deprecated("use addRegularLectureOnTimetable and addCustomLectureOnTimetable instead")
    @POST(URLConstant.TIMETABLE.V2.LECTURE.LECTURE)
    suspend fun postTimetableLectures(
        @Body lectures: LecturesQueryRequest
    ): TimetableLecturesResponse

    // 시간표에 새로운 정규 강의 추가
    @POST(URLConstant.TIMETABLE.V3.LECTURE.REGULER)
    suspend fun addRegularLectureOnTimetable(
        @Body regularLectureRequest: TimetableRegularLectureRequest
    ): TimetableLecturesResponseV3

    // 시간표에 새로운 커스텀 강의 추가
    @POST(URLConstant.TIMETABLE.V3.LECTURE.CUSTOM)
    suspend fun addCustomLectureOnTimetable(
        @Body customLectureRequest: TimetableCustomLectureRequest
    ): TimetableLecturesResponseV3

    // 프레임 수정
    @Deprecated("use editFrame instead")
    @PUT(URLConstant.TIMETABLE.V2.FRAME.ID)
    suspend fun putTimetableFrame(
        @Path("id") id: Int,
        @Body frame: TimetableFrameQueryRequest
    ): TimetableFrameResponse

    @PUT(URLConstant.TIMETABLE.V3.FRAME.ID)
    suspend fun editFrame(
        @Path("id") frameId: Int,
        @Query("name") frameName: String,
        @Query("is_main") isMain: Boolean
    ): List<TimetableFrameResponseV3>

    // 프레임 생성
    @Deprecated("use createFrame instead")
    @POST(URLConstant.TIMETABLE.V2.FRAME.ID)
    suspend fun postTimetableFrame(
        @Body frame: TimetableFrameCreateQueryRequest
    ): TimetableFrameResponse

    @POST(URLConstant.TIMETABLE.V3.FRAME.ID)
    suspend fun createFrame(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<TimetableFrameResponseV3>

    @Deprecated("use restoreFrameByFrameId instead")
    @POST(URLConstant.TIMETABLE.V2.FRAME.ROLLBACK)
    suspend fun postRollbackFrame(
        @Query("timetable_frame_id") frameId: Int
    ): TimetableLecturesResponse

    // 삭제한 시간표 복구
    @POST(URLConstant.TIMETABLE.V3.FRAME.ROLLBACK)
    suspend fun restoreFrameByFrameId(
        @Query("timetable_frame_id") frameId: Int
    ): TimetableLecturesResponseV3

    // 학기의 모든 프레임 삭제
    @DELETE(URLConstant.TIMETABLE.V3.FRAME.FRAMES)
    suspend fun deleteFramesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): Response<Unit>

    @DELETE(URLConstant.TIMETABLE.V2.FRAME.FRAME)
    suspend fun deleteTimetableFrame(
        @Query("id") frameId: Int
    ): Response<Unit>

    /**
     * @param semester 학기명
     * @return 학기의 프레임 리스트
     */
    @Deprecated("use getFramesBySemester instead")
    @GET(URLConstant.TIMETABLE.V2.FRAME.FRAMES)
    suspend fun getTimetableFrames(
        @Query("semester") semester: String
    ): List<TimetableFrameResponse>

    // 학기에 있는 프레임들 조회
    @GET(URLConstant.TIMETABLE.V3.FRAME.FRAME)
    suspend fun getFramesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<TimetableFrameResponseV3>

    /**
     * 학생이 추가한 모든 학기의 프레임을 불러옴
     * @return 학생이 추가한 모든 시간표 프레임
     */
    @Deprecated("use getAllFramesV3 instead")
    @GET(URLConstant.TIMETABLE.V2.FRAME.FRAMES)
    suspend fun getAllFrames(): TimetableFramesResponse

    @GET(URLConstant.TIMETABLE.V3.FRAME.FRAMES)
    suspend fun getAllFramesV3(): TimetableFramesResponseV3

    @DELETE(URLConstant.TIMETABLE.V2.LECTURE.ID)
    suspend fun deleteTimetableLecture(
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE(URLConstant.TIMETABLE.V2.FRAME.LECTURE)
    suspend fun deleteTimetableFrameLecture(
        @Path("frameId") frameId: Int,
        @Path("lectureId") lectureId: Int
    ): Response<Unit>

    @DELETE(URLConstant.TIMETABLE.V2.LECTURE.LECTURES)
    suspend fun deleteTimetableLectures(
        @Query("timetable_lecture_ids") lectureIds: List<Int>
    ): Response<Unit>

    @Deprecated("use deleteFramesBySemester instead")
    @DELETE(URLConstant.TIMETABLE.V2.FRAME.ALL)
    suspend fun deleteAllTimetableFrame(
        @Query("semester") semester: String
    ): Response<Unit>
}
