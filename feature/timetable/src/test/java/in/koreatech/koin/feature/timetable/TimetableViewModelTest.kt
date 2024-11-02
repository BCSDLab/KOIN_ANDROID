package `in`.koreatech.koin.feature.timetable

import app.cash.turbine.test
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.GetLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.feature.timetable.viewmodel.TimetableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import okhttp3.Dispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.http.GET

/**
 * 뷰모델, 유즈케이스, 레포지토리 테스트 연습해보기
 * 추가로 플로우 테스트도 진행해보기
 */

class TimetableViewModelTest {
    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TimetableViewModel
    private lateinit var timetableRepository: TimetableRepository
    private lateinit var getLectureUseCase: GetLecturesUseCase
    private lateinit var getSemesterUseCase: GetSemesterUseCase
    private lateinit var getTimetableFrameUseCase: GetTimetableFramesUseCase

    @Before
    fun setUp() {
        timetableRepository = FakeTimetableRepository()
        getLectureUseCase = GetLecturesUseCase(timetableRepository)
        getSemesterUseCase = GetSemesterUseCase(timetableRepository)
        getTimetableFrameUseCase = GetTimetableFramesUseCase(timetableRepository)

        viewModel = TimetableViewModel(
            getLecturesUseCase = getLectureUseCase,
            getSemesterUseCase = getSemesterUseCase,
            getTimetableFramesUseCase = getTimetableFrameUseCase,
            timetableRepository  = timetableRepository
        )
    }

    @Test
    fun `학기(semester) 받아오는 플로우`() = runTest {
        getSemesterUseCase().test {
            advanceTimeBy(400)
            assertEquals(currentTime, 400)
            assertEquals(awaitItem(), listOf(SampleTimetable.semester))
            awaitComplete()
        }
    }

    @Test
    fun `수업(lecture) 받아오는 플로우`() = runTest {
        getLectureUseCase("20242").test {
            advanceTimeBy(600)
            assertEquals(currentTime, 600)
            assertEquals(awaitItem(), listOf(SampleTimetable.lecture))
            awaitComplete()
        }
    }


    @Test
    fun `학기(semester), 수업(lecture)을 순서대로 받아오는 플로우`() = runTest {
        val semesters = getSemesterUseCase().firstOrNull().orEmpty()
        val semester = semesters.firstOrNull()?.semester.orEmpty()
        val lecture = getLectureUseCase(semester).firstOrNull().orEmpty()

        assertEquals(currentTime, 1000)
        assertEquals(lecture, listOf(SampleTimetable.lecture))
        // 샘플 코드를 싱글톤을 사용해서 다른 곳에 있는데 이게 실질적으로 맞는가?
        // 위와 같은 질문을 한 이유는? 여기에다가 직접적으로 넣어서 테스트 해보는 것이 더 직관적일 것 같다는 생각이 든다.
    }

    @Test
    fun `뷰모델 테스트하기`() = runTest {
        // 뷰모델은 어떻게 테스트 하는가?
    }
}